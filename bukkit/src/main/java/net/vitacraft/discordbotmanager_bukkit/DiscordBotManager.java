package net.vitacraft.discordbotmanager_bukkit;

import lombok.Getter;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager_bukkit.commands.DBMCommand;
import net.vitacraft.discordbotmanager_bukkit.message.Messenger;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordBotManager extends JavaPlugin  {
    @Getter
    private static DiscordBotManager plugin;
    @Getter
    private Common common;

    @Override
    public void onEnable() {
        plugin = this;
        common = new Common("bukkit", new Messenger(this));
        getCommand("runcmd").setExecutor(new DBMCommand(this));
    }

    @Override
    public void onDisable() {

    }

}
