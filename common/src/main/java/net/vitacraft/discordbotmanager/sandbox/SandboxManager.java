package net.vitacraft.discordbotmanager.sandbox;

import net.vitacraft.discordbotmanager.message.Messenger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class SandboxManager {
    private final Messenger messenger;
    private final Map<String, Sandbox> sandboxes = new HashMap<>();

    public SandboxManager(Messenger messenger) {
        this.messenger = messenger;
    }

    public void registerSandbox(Sandbox sandbox) {
        sandboxes.put(sandbox.getName(), sandbox);
    }

    public boolean startSandbox(String name) {
        Sandbox sandbox = sandboxes.get(name);
        if (sandbox != null) {
            try {
                sandbox.start();
                messenger.info("JAR process " + name + " started successfully.");
                return true;
            } catch (IOException e) {
                messenger.error("Could not start JAR process " + name, e);
            } catch (IllegalStateException e) {
                messenger.error(e.getMessage());
            }
        } else {
            messenger.info("No sandbox found with name " + name);
        }
        return false;
    }

    public boolean stopSandbox(String name) {
        Sandbox sandbox = sandboxes.get(name);
        if (sandbox != null) {
            try {
                sandbox.stop();
                messenger.info("JAR process " + name + " stopped successfully.");
                return true;
            } catch (InterruptedException e) {
                messenger.error("Error stopping JAR process " + name, e);
            } catch (IllegalStateException e) {
                messenger.error(e.getMessage());
            }
        } else {
            messenger.info("No sandbox found with name " + name);
        }
        return false;
    }

    public void stopAllSandboxes() {
        for (Sandbox sandbox : sandboxes.values()) {
            try {
                sandbox.stop();
                messenger.info("JAR process " + sandbox.getName() + " stopped successfully.");
            } catch (InterruptedException e) {
                messenger.error("Error stopping JAR process " + sandbox.getName(), e);
            } catch (IllegalStateException e) {
                messenger.error(e.getMessage());
            }
        }
    }
}