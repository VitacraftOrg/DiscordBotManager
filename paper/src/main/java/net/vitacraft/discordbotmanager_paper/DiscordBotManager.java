package net.vitacraft.discordbotmanager_paper;

import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager_paper.message.Messenger;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordBotManager extends JavaPlugin {

    @Override
    public void onEnable() {
        Common common = new Common("paper", new Messenger(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
