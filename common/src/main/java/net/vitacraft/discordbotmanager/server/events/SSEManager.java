package net.vitacraft.discordbotmanager.server.events;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SSEManager {
    private final ConcurrentMap<String, List<OutputStream>> endpointClients = new ConcurrentHashMap<>();

    public synchronized void addClient(String endpoint, OutputStream client) {
        endpointClients.computeIfAbsent(endpoint, k -> Collections.synchronizedList(new ArrayList<>())).add(client);
    }

    public synchronized void removeClient(String endpoint, OutputStream client) {
        List<OutputStream> clients = endpointClients.get(endpoint);
        if (clients != null) {
            clients.remove(client);
            if (clients.isEmpty()) {
                endpointClients.remove(endpoint);
            }
        }
    }

    public void sendEvent(String endpoint, String event, String data) {
        List<OutputStream> clients = endpointClients.get(endpoint);
        if (clients != null) {
            for (OutputStream client : clients) {
                try {
                    sendEvent(client, event, data);
                } catch (IOException e) {
                    e.printStackTrace();
                    removeClient(endpoint, client);
                }
            }
        }
    }

    private void sendEvent(OutputStream writer, String event, String data) throws IOException {
        String eventData = "event: " + event + "\ndata: " + data + "\n\n";
        writer.write(eventData.getBytes(StandardCharsets.UTF_8));
        writer.flush();
    }
}

