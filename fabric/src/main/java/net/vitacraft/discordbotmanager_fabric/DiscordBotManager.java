package net.vitacraft.discordbotmanager_fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager_fabric.message.Messenger;

public class DiscordBotManager implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Common common = new Common("fabric", new Messenger());
    }
}
