package net.vitacraft.discordbotmanager.server;

import com.sun.net.httpserver.HttpServer;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.server.endpoints.GetSandboxes;
import net.vitacraft.discordbotmanager.server.endpoints.SandboxConsoleSSE;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

import static com.sun.net.httpserver.SimpleFileServer.createFileHandler;

public class Server {
    private final Common common;
    private final int port;
    private final Path destinationFolderPath;
    private HttpServer httpServer;

    public Server(Common common, String resourceFolderPath, int port) {
        this.common = common;
        this.port = port;
        File resourceDestinationDir = new File(common.getWorkDir().getAbsolutePath() + "/" + resourceFolderPath);
        if (!resourceDestinationDir.exists()) {
            resourceDestinationDir.mkdirs();
        }
        this.destinationFolderPath = new File(common.getWorkDir(), resourceFolderPath).toPath().toAbsolutePath();
        new ServerProvider(resourceFolderPath, destinationFolderPath);
    }

    public void start() {
        try {
            // Create the HttpServer instance
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.setExecutor(null);
            httpServer.createContext("/", createFileHandler(destinationFolderPath));

            RoutesManager routesManager = new RoutesManager(httpServer, common);
            routesManager.addEndpoint(new GetSandboxes());
            routesManager.addEndpoint(new SandboxConsoleSSE());

            routesManager.init();

            httpServer.start();
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
            System.out.println("Server stopped");
        }
    }
}
