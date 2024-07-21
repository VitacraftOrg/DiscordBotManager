package net.vitacraft.discordbotmanager.storage;

import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.config.ConfigUtil;
import net.vitacraft.discordbotmanager.sandbox.Sandbox;
import net.vitacraft.discordbotmanager.sandbox.Settings;
import org.simpleyaml.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final ConfigUtil configUtil;

    public Storage(Common common){
        String folderName = "/";
        if (common.getInstanceStringReference().equals("fabric")) {
            folderName += "mods";
        } else {
            folderName += "plugins";
        }

        configUtil = new ConfigUtil(System.getProperty("user.dir") + folderName + "/DiscordBotManager/bots.yml");
        configUtil.save();
    }

    public List<Sandbox> retrieveAllSandboxes(){
        List<Sandbox> sandboxes = new ArrayList<>();
        for (String box : configUtil.getConfig().getKeys(false)){
            sandboxes.add(retrieveSandbox(box));
        }
        return sandboxes;
    }

    public Sandbox retrieveSandbox(String name){
        ConfigurationSection file = configUtil.getConfig().getConfigurationSection(name);
        return new Sandbox(
                new Settings(
                        name,
                        file.getString("filePath"),
                        file.getStringList("flags"),
                        file.getInt("maxRam"),
                        file.getBoolean("autostart"))
        );
    }
}
