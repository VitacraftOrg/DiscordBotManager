package net.vitacraft.discordbotmanager.sandbox;

import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.message.Messenger;
import java.util.HashMap;
import java.util.Map;
public class SandboxManager {
    private final Messenger messenger;
    private final Map<String, Sandbox> sandboxes = new HashMap<>();
    private final Common common;
    private final ResourceManager resourceManager;

    public SandboxManager(Messenger messenger, Common common) {
        this.messenger = messenger;
        this.common = common;
        this.resourceManager = new ResourceManager(common);
    }

    public void registerSandbox(Sandbox sandbox) {
        if(resourceManager.canFitMemoryQuota(sandbox.getSettings().ram())){
            sandboxes.put(sandbox.getSettings().name(), sandbox);
        }
    }

    public boolean startSandbox(String name) {
        Sandbox sandbox = sandboxes.get(name);
        if (sandbox != null) {
            sandbox.start();
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
                messenger.info("JAR process " + sandbox.getSettings().name() + " stopped successfully.");
            } catch (InterruptedException e) {
                messenger.error("Error stopping JAR process " + sandbox.getSettings().name(), e);
            } catch (IllegalStateException e) {
                messenger.error(e.getMessage());
            }
        }
    }

    public Map<String, Sandbox> getAllSandboxes() {
        return sandboxes;
    }
}