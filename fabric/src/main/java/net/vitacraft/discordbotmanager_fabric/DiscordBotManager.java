package net.vitacraft.discordbotmanager_fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.vitacraft.discordbotmanager.Common;

public class DiscordBotManager implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Common common = new Common("fabric", null);
    }
}
