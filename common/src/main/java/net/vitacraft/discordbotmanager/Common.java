package net.vitacraft.discordbotmanager;

import net.vitacraft.discordbotmanager.message.Messenger;
import net.vitacraft.discordbotmanager.sandbox.Sandbox;
import net.vitacraft.discordbotmanager.sandbox.SandboxManager;

import java.util.ArrayList;
import java.util.List;

public class Common {
    private final String irs;
    private final SandboxManager sbm;

    public Common(String instanceReferenceString, Messenger messenger){
        this.irs = instanceReferenceString;
        this.sbm = new SandboxManager(messenger);
    }

    public void registerSandbox(Sandbox sandbox){
        sbm.registerSandbox(sandbox);
    }

    public void registerSandbox(String name, String jarPath, List<String> jvmArgs){
        sbm.registerSandbox(new Sandbox(name, jarPath, jvmArgs));
    }

    public boolean toggleSandbox(String name, boolean action){
        boolean success;
        if (action){
            success = sbm.startSandbox(name);
        } else {
            success = sbm.stopSandbox(name);
        }
        return success;
    }
}
