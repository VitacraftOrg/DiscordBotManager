package net.vitacraft.discordbotmanager.message;
public interface Messenger {
    void info(String info);
    void error(String errorReadingJarOutput, Exception e);
    void error(String errorReadingJarOutput);
}
