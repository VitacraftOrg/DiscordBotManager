package net.vitacraft.discordbotmanager_bukkit;

import lombok.Getter;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager_bukkit.commands.JLCommand;
import net.vitacraft.discordbotmanager_bukkit.message.Messenger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DiscordBotManager extends JavaPlugin  {
    @Getter
    private static DiscordBotManager plugin;
    @Getter
    private Common common;

    @Override
    public void onEnable() {
        plugin = this;
        common = new Common("bukkit", new Messenger(this));
        Objects.requireNonNull(getCommand("jl")).setExecutor(new JLCommand(common.getSandboxManager()));
    }

    @Override
    public void onDisable() {

    }

}
