package net.vitacraft.discordbotmanager.sandbox;

import java.util.List;

public record Settings (String name, String jarPath, List<String> jvmArgs, int ram, boolean autostart) {
}
