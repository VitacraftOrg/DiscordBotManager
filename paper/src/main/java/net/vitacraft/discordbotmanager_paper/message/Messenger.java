package net.vitacraft.discordbotmanager_paper;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class Messenger implements net.vitacraft.discordbotmanager.message.Messenger {
    private final Plugin plugin;

    public Messenger(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void info(String info) {
        plugin.getLogger().info(info);
    }

    @Override
    public void error(String errorReadingJarOutput, Exception e) {
        plugin.getLogger().log(Level.SEVERE, errorReadingJarOutput + " : " + e);
    }

    @Override
    public void error(String errorReadingJarOutput) {
        plugin.getLogger().log(Level.SEVERE, errorReadingJarOutput);
    }
}
