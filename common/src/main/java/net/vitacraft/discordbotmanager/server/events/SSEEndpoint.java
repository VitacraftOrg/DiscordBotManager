package net.vitacraft.discordbotmanager.server.events;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.server.Endpoint;

import java.io.IOException;
import java.io.OutputStream;

public abstract class SSEEndpoint implements Endpoint {
    private final SSEManager sseManager;
    private final String endpointUrl;

    public SSEEndpoint(String endpointUrl) {
        this.sseManager = new SSEManager();
        this.endpointUrl = endpointUrl;
    }

    @Override
    public String getEndpointUrl() {
        return endpointUrl;
    }

    @Override
    public void handle(Common common, HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "text/event-stream");
        responseHeaders.add("Cache-Control", "no-cache");
        responseHeaders.add("Connection", "keep-alive");

        exchange.sendResponseHeaders(200, 0);
        OutputStream writer = exchange.getResponseBody();

        sseManager.addClient(endpointUrl, writer);

        try {
            processClient(common, exchange);
        } finally {
            sseManager.removeClient(endpointUrl, writer);
        }
    }

    protected abstract void processClient(Common common, HttpExchange exchange) throws IOException;

    protected void sendEvent(String event, String data) {
        sseManager.sendEvent(endpointUrl, event, data);
    }

    protected String getQueryParameter(HttpExchange exchange, String parameterName) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            return null;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(parameterName)) {
                return keyValue[1];
            }
        }
        return null;
    }

    protected void keepConnectionOpen() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
