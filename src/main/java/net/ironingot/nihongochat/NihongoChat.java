package net.ironingot.nihongochat;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "NihongoChat", name = "NihongoChat")
public class NihongoChat {
    public Logger logger;
    private File config;
    private ConfigHandler configHandler;

    @SpongeEventHandler
    public void onInit(PreInitializationEvent event) {
        logger = event.getPluginLog();
        config = event.getSuggestedConfigurationFile();
    }

    @SpongeEventHandler
    public void onStart(ServerStartingEvent event) {
        event.getGame().getEventManager().register(new NihongoChatAsyncPlayerChatListener(this));

        loadConfig();
        // getCommand("nihongochat").setExecutor(new NihongoChatCommand(this));

        // logger.info(getDescription().getName() + "-" +
        //            getDescription().getVersion() + " is enabled!");
    }

    @SpongeEventHandler
    public void onStop(ServerStoppingEvent event) {
        // logger.info(getDescription().getName() + " is disabled");
    }

    public void loadConfig() {
        try {
            config.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configHandler = new ConfigHandler(config);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
