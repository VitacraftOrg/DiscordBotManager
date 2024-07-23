package net.vitacraft.discordbotmanager.server.endpoints;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.sandbox.Sandbox;
import net.vitacraft.discordbotmanager.server.events.SSEEndpoint;

import java.io.IOException;

public class SandboxConsoleSSE extends SSEEndpoint {
    public SandboxConsoleSSE() {
        super("/api/sandbox-console");
    }

    @Override
    protected void processClient(Common common, HttpExchange exchange) throws IOException {
        String sandboxId = getQueryParameter(exchange, "id");
        Sandbox sandbox = common.getSandboxManager().getAllSandboxes().get(sandboxId);

        if (sandbox == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        sandbox.getProcessInteractor().registerListener(message -> {
            JsonObject data = new JsonObject();
            data.addProperty("message", message);
            sendEvent("console-output", data.toString());
        });

        keepConnectionOpen();
    }
}



