package net.vitacraft.discordbotmanager_bukkit;

import net.vitacraft.discordbotmanager.CommonClass;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordBotManager extends JavaPlugin  {

    @Override
    public void onEnable() {
        CommonClass cc = new CommonClass("bukkit");
        cc.testMethod();
    }

    @Override
    public void onDisable() {

    }

}
