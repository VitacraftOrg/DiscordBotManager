package net.vitacraft.discordbotmanager_fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.vitacraft.discordbotmanager.CommonClass;

public class DiscordBotManager implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        CommonClass cc = new CommonClass("fabric");
        cc.testMethod();
    }
}
