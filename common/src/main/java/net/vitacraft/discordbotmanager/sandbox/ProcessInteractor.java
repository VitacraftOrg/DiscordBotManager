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
    private OutputStream outputStream;
    private ExecutorService executorService;
    private final List<OutputListener> outputListeners;

    public ProcessInteractor(Process process) {
        this.process = process;
        this.outputListeners = new ArrayList<>();
    }

    public void start() {
        outputStream = process.getOutputStream();
        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (outputListeners) {
                        for (OutputListener listener : outputListeners) {
                            listener.onOutput(line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    public void stop() throws InterruptedException {
        if (process.isAlive()) {
            process.destroy();
            process.waitFor();
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}


