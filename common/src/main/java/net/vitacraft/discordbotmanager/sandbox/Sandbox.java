package net.vitacraft.discordbotmanager.sandbox;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Sandbox {
    private Process process;
    @Getter
    private final String name;
    @Getter
    private final String jarPath;
    @Getter
    private final List<String> jvmArgs;
    @Getter
    private Status status;

    public Sandbox(String name, String jarPath, List<String> jvmArgs) {
        this.name = name;
        this.jarPath = jarPath;
        this.jvmArgs = jvmArgs;
        this.status = Status.STOPPED;
    }

    public void start() throws IOException {
        if (status == Status.RUNNING) {
            throw new IllegalStateException("JAR process " + name + " is already running!");
        }

        List<String> command = new ArrayList<>();
        command.add("java");
        command.addAll(jvmArgs);
        command.add("-jar");
        command.add(jarPath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
        status = Status.RUNNING;

        // Capture output from the JAR
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[" + name + "] " + line);
                }
            } catch (IOException e) {
                status = Status.ERROR;
            }
        }).start();
    }

    public void stop() throws InterruptedException {
        if (status != Status.RUNNING) {
            throw new IllegalStateException("JAR process " + name + " is not running!");
        }
        process.destroy();
        process.waitFor();
        status = Status.STOPPED;
    }
}
