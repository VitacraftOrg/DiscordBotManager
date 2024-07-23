package net.vitacraft.discordbotmanager.server;

import com.sun.net.httpserver.HttpServer;
import net.vitacraft.discordbotmanager.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutesManager {
    private final HttpServer server;
    private final Common common;
    private final List<Endpoint> endpoints;

    public RoutesManager(HttpServer server, Common common) {
        this.server = server;
        this.common = common;
        this.endpoints = new ArrayList<>();
    }

    public void addEndpoint(Endpoint endpoint) {
        endpoints.add(endpoint);
    }

    public void init() {
        for (Endpoint endpoint : endpoints) {
            server.createContext(endpoint.getEndpointUrl(), exchange -> {
                try {
                    endpoint.handle(common, exchange);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

