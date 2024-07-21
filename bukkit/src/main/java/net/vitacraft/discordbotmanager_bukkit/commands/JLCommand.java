package net.vitacraft.discordbotmanager_bukkit.commands;

import net.vitacraft.discordbotmanager.commands.CommandInterpreter;
import net.vitacraft.discordbotmanager.sandbox.SandboxManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class JLCommand extends CommandInterpreter implements CommandExecutor {


    public JLCommand(SandboxManager sbm){
        super(sbm);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        String response = "";
        try {
            response = interpret(List.of(args));
        } catch (IllegalArgumentException e){
            System.out.println("GUI needs to be opend");
        }
        sender.sendMessage(response);
        return true;
    }
}

