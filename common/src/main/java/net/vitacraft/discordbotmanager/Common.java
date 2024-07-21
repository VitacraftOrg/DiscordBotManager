package net.vitacraft.discordbotmanager;

import lombok.Getter;
import lombok.NonNull;
import net.vitacraft.discordbotmanager.config.ConfigUtil;
import net.vitacraft.discordbotmanager.message.Messenger;
import net.vitacraft.discordbotmanager.sandbox.Sandbox;
import net.vitacraft.discordbotmanager.sandbox.SandboxManager;
import net.vitacraft.discordbotmanager.storage.Storage;
import java.io.File;

public class Common {
    private final String irs;
    private final SandboxManager sbm;
    @Getter
    private static File workDir;

    public Common(String instanceReferenceString,@NonNull Messenger messenger){
        String folderName = "/";
        if (instanceReferenceString.equals("fabric")) {
            folderName += "mods";
        } else {
            folderName += "plugins";
        }
        messenger.info(instanceReferenceString + " works");
        workDir = new File(System.getProperty("user.dir") + folderName + "/DiscordBotManager");

        if(!workDir.exists()){
            if(!workDir.mkdirs()){
                messenger.error("There was an issue generating the workDir for this instance");
            }
        }

        ConfigUtil cu = new ConfigUtil(workDir.getAbsolutePath() + "/config.yml");
        cu.save();
        this.irs = instanceReferenceString;
        this.sbm = new SandboxManager(messenger, this);
        Storage storage = new Storage(this);
        for (Sandbox s : storage.retrieveAllSandboxes()){
            sbm.registerSandbox(s);
            if (s.getSettings().autostart()){
                sbm.startSandbox(s.getSettings().name());
            }
        }
    }

    public SandboxManager getSandboxManager(){
        return sbm;
    }

    public String getInstanceStringReference(){
        return irs;
    }

    public void shutdown(){
        sbm.stopAllSandboxes();
    }
}
