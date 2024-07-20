package net.vitacraft.discordbotmanager_fabric.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

public class Messenger implements net.vitacraft.discordbotmanager.message.Messenger {
    private final Logger logger;

    public Messenger(){
        this.logger = LogManager.getLogger(Messenger.class);
    }
    @Override
    public void info(String info) {
        logger.info(info);
    }

    @Override
    public void error(String errorReadingJarOutput, Exception e) {
        logger.log(Level.ERROR, e.getMessage());
    }

    @Override
    public void error(String errorReadingJarOutput) {
        logger.log(Level.ERROR, errorReadingJarOutput);
    }
}
