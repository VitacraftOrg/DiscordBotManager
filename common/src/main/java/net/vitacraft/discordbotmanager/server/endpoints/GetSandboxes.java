package net.vitacraft.discordbotmanager.server.endpoints;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.sandbox.Sandbox;
import net.vitacraft.discordbotmanager.sandbox.Settings;
import net.vitacraft.discordbotmanager.server.Endpoint;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

public class GetSandboxes implements Endpoint {
    @Override
    public String getEndpointUrl() {
        return "/api/getsandboxes";
    }

    @Override
    public void handle(Common common, HttpExchange exchange) throws IOException {
        Map<String, Sandbox> sandboxList = common.getSandboxManager().getAllSandboxes();

        // Convert sandboxList to JSON
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, Sandbox> entry : sandboxList.entrySet()) {
            Sandbox sandbox = entry.getValue();
            Settings settings = sandbox.getSettings();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ram", settings.ram());
            jsonObject.addProperty("jarPath", settings.jarPath());
            jsonObject.addProperty("name", settings.name());
            jsonObject.addProperty("autostart", settings.autostart());
            jsonObject.addProperty("jvmArgs", Arrays.toString(settings.jvmArgs().toArray()));
            jsonObject.addProperty("startTime", sandbox.getStartTime());
            jsonObject.addProperty("status", sandbox.getStatus().toString());

            jsonArray.add(jsonObject);
        }

        String response = jsonArray.toString();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
