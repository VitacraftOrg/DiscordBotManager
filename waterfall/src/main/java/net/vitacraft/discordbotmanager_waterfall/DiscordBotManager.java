package net.vitacraft.discordbotmanager_waterfall;

import net.md_5.bungee.api.plugin.Plugin;
import net.vitacraft.discordbotmanager.Common;

public final class DiscordBotManager extends Plugin {

    @Override
    public void onEnable() {
        Common common = new Common("waterfall", null);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
