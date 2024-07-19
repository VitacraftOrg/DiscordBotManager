package net.vitacraft.discordbotmanager_paper;

import net.vitacraft.discordbotmanager.Common;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordBotManager extends JavaPlugin {

    @Override
    public void onEnable() {
        Common common = new Common("paper", null);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
