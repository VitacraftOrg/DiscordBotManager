package net.vitacraft.discordbotmanager.server;

import com.sun.net.httpserver.HttpExchange;
import net.vitacraft.discordbotmanager.Common;

import java.io.IOException;

public interface Endpoint {
    String getEndpointUrl();
    void handle(Common common, HttpExchange exchange) throws IOException;
}
