package net.vitacraft.discordbotmanager.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * A utility class for extracting and serving static resources from either a JAR file or a directory.
 * This class handles the extraction of resources specified by a resource folder path to a destination folder.
 */
public class ServerProvider {
    private final String resourceFolderPath;
    private final Path destinationFolderPath;

    /**
     * Constructs a new ServerProvider instance.
     *
     * @param resourceFolderPath the path to the resource folder inside the JAR or classpath.
     * @param destinationFolderPath the path where resources should be extracted to.
     */
    public ServerProvider(String resourceFolderPath, Path destinationFolderPath) {
        this.resourceFolderPath = resourceFolderPath;
        this.destinationFolderPath = destinationFolderPath;
        try {
            extractResourceFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts the resource folder to the destination folder. It checks if the resource is in a JAR or a directory.
     *
     * @throws IOException if an I/O error occurs during extraction.
     */
    private void extractResourceFolder() throws IOException {
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(ServerProvider.class.getClassLoader());
            if (!Files.exists(destinationFolderPath)) {
                Files.createDirectories(destinationFolderPath);
            }
            String resourcePath = Objects.requireNonNull(ServerProvider.class.getResource(resourceFolderPath)).toString();
            if (resourcePath.startsWith("jar:")) {
                extractFromJar(resourceFolderPath, destinationFolderPath.toString());
            } else {
                extractFromDirectory(resourceFolderPath, destinationFolderPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    /**
     * Extracts resources from a JAR file to the specified destination folder.
     *
     * @param resourceFolderPath the path to the resource folder inside the JAR.
     * @param destinationFolderPath the path where resources should be extracted to.
     * @throws IOException if an I/O error occurs during extraction.
     */
    private static void extractFromJar(String resourceFolderPath, String destinationFolderPath) throws IOException {
        String jarPath = ServerProvider.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(resourceFolderPath.substring(1))) {
                    Path filePath = Paths.get(destinationFolderPath, entryName.substring(resourceFolderPath.length() - 1));
                    if (entry.isDirectory()) {
                        Files.createDirectories(filePath);
                    } else {
                        try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
                            Files.copy(entryInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
    }

    /**
     * Extracts resources from a directory to the specified destination folder.
     *
     * @param resourceFolderPath the path to the resource folder inside the classpath.
     * @param destinationFolderPath the path where resources should be extracted to.
     * @throws IOException if an I/O error occurs during extraction.
     * @throws URISyntaxException if the resource path cannot be converted to a URI.
     */
    private static void extractFromDirectory(String resourceFolderPath, Path destinationFolderPath) throws IOException, URISyntaxException {
        Path resourceFolder = Paths.get(Objects.requireNonNull(ServerProvider.class.getResource(resourceFolderPath)).toURI());
        try (Stream<Path> paths = Files.walk(resourceFolder)) {
            paths.forEach(path -> {
                Path targetPath = destinationFolderPath.resolve(resourceFolder.relativize(path).toString());
                try {
                    if (Files.isDirectory(path)) {
                        if (!Files.exists(targetPath)) {
                            Files.createDirectories(targetPath);
                        }
                    } else {
                        Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
