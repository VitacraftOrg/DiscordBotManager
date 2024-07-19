package net.vitacraft.discordbotmanager_velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import net.vitacraft.discordbotmanager.Common;
import org.slf4j.Logger;

@Plugin(
        id = "discordbotmanager",
        name = "DBM-Velo",
        version = "1.0"
)
public class DiscordBotManager {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Common common = new Common("velocity", null);
    }
}
