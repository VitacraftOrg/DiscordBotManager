package net.vitacraft.discordbotmanager_bukkit;

import net.vitacraft.discordbotmanager.Common;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordBotManager extends JavaPlugin  {

    @Override
    public void onEnable() {
        Common common = new Common("bukkit", null);
    }

    @Override
    public void onDisable() {

    }

}
