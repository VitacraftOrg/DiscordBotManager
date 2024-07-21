package net.vitacraft.discordbotmanager.sandbox;

import lombok.Getter;
import net.vitacraft.discordbotmanager.Common;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Sandbox {
    private Common common;
    private Process process;
    @Getter
    private Settings settings;
    @Getter
    private Status status;
    @Getter
    private Thread thread;
    private ProcessInteractor processInteractor;

    public Sandbox(String name, String jarPath, int ram, List<String> jvmArgs) {
        this.status = Status.STOPPED;
        this.settings = new Settings(name, jarPath, jvmArgs, ram, false);
    }

    public Sandbox(Common common, Settings settings) {
        this.common = common;
        this.status = Status.STOPPED;
        this.settings = settings;
    }

    public void start() {
        ProcessBuilder processBuilder = getProcessBuilder();
        thread = new Thread(() -> {
            try {
                process = processBuilder.start();
                processInteractor = new ProcessInteractor(this, process);

                status = Status.RUNNING;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            /*try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[" + settings.name() + "] " + line);
                }
            } catch (IOException e) {
                status = Status.ERROR;
            }*/
        });

        thread.start();
    }

    @NotNull
    private ProcessBuilder getProcessBuilder() {
        if (status == Status.RUNNING) {
            throw new IllegalStateException("JAR process " + settings.name() + " is already running!");
        }

        File jarFile = new File(settings.jarPath());
        if (!jarFile.exists()) {
            throw new IllegalStateException("The JAR file does not exist: " + jarFile.getAbsolutePath());
        }

        String[] command = {
                "java",
                "-Xms" + (settings.ram() / 2) + "M",
                "-Xmx" + settings.ram() + "M",
                "-jar",
                jarFile.getAbsolutePath(),
                "-nogui"
        };

        ProcessBuilder builder = new ProcessBuilder(command);
        File workDir = new File(Common.getWorkDir() + "/" + settings.name() + "/");

        if (!workDir.exists()) {
            if (!workDir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + workDir.getAbsolutePath());
            }
        }

        builder.directory(workDir);
        return builder;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public ProcessInteractor getProcessInteractor() {
        if(status != Status.RUNNING){
            throw new IllegalStateException("Sandbox not running, can't retrieve it's ProcessInteractor");
        }
        if (processInteractor == null) {
            throw new IllegalStateException("ProcessInteractor is null.");
        }
        return processInteractor;
    }

    public void stop() throws InterruptedException {
        if (status != Status.RUNNING) {
            throw new IllegalStateException("JAR process " + settings.name() + " is not running!");
        }
        process.destroy();
        process.waitFor();
        status = Status.STOPPED;
    }
}

