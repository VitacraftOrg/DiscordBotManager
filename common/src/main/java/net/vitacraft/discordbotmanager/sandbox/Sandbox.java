package net.vitacraft.discordbotmanager.sandbox;

import lombok.Getter;
import lombok.Setter;
import net.vitacraft.discordbotmanager.Common;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Sandbox {
    @Getter
    private Process process;
    @Getter
    private Settings settings;
    @Setter
    @Getter
    private Status status;
    @Getter
    private Thread thread;
    @Getter
    private double startTime;
    private ProcessInteractor processInteractor; // Add member variable for ProcessInteractor

    public Sandbox(Settings settings) {
        this.status = Status.STOPPED;
        this.settings = settings;
    }

    public Sandbox(String name, String jarPath) {
        this.status = Status.STOPPED;
        this.settings = new Settings(name, jarPath, new ArrayList<>(), 256, false);
    }

    public void start() {
        ProcessBuilder processBuilder = getProcessBuilder();
        thread = new Thread(() -> {
            startTime = System.currentTimeMillis();
            try {
                process = processBuilder.start();
                processInteractor = new ProcessInteractor(this, process);

                status = Status.RUNNING;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

