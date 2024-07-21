package net.vitacraft.discordbotmanager.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ProcessInteractor {
    private final Process process;
    private final Sandbox sandbox;
    private OutputStream outputStream;
    private final List<OutputListener> outputListeners;

    public ProcessInteractor(Sandbox sandbox, Process process) {
        this.process = process;
        this.sandbox = sandbox;
        this.outputListeners = new ArrayList<>();
        start();
    }

    private void start() {
        outputStream = process.getOutputStream();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //debug
                    System.out.println("[" + sandbox.getSettings().name() + "] " + line);
                    synchronized (outputListeners) {
                        for (OutputListener listener : outputListeners) {
                            listener.onOutput(line);
                        }
                    }
                }
            } catch (IOException e) {
                sandbox.setStatus(Status.ERROR);
            }
        });
    }

    public void sendCommand(String command) throws IOException {
        if (!process.isAlive()) {
            throw new IllegalStateException("The process is not running.");
        }

        outputStream.write((command + "\n").getBytes());
        outputStream.flush();
    }

    public void registerListener(OutputListener listener) {
        synchronized (outputListeners) {
            outputListeners.add(listener);
        }
    }

    public void unregisterListener(OutputListener listener) {
        synchronized (outputListeners) {
            outputListeners.remove(listener);
        }
    }
}