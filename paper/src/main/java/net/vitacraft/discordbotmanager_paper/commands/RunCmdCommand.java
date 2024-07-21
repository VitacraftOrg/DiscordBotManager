package net.vitacraft.discordbotmanager_paper.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.sandbox.ProcessInteractor;
import net.vitacraft.discordbotmanager.sandbox.Sandbox;
import net.vitacraft.discordbotmanager.sandbox.Status;
import net.vitacraft.discordbotmanager_paper.DiscordBotManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class RunCmdCommand implements CommandExecutor {
    private final DiscordBotManager plugin;

    public RunCmdCommand(DiscordBotManager plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Check for the correct number of arguments
        if (args.length < 2) {
            sender.sendMessage("Usage: /runcmd <sandboxName> <commandToSend>");
            return false;
        }

        String sandboxName = args[0];
        String commandToSend = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Common common = plugin.getCommon();

        Map<String, Sandbox> sandboxes = common.getSandboxManager().getAllSandboxes();
        Sandbox sandbox = sandboxes.get(sandboxName);

        if (sandbox == null) {
            sender.sendMessage("Sandbox with name '" + sandboxName + "' does not exist.");
            return false;
        }

        try {
            if (sandbox.getStatus() != Status.RUNNING) {
                sender.sendMessage("Sandbox '" + sandboxName + "' is not running.");
                return false;
            }

            ProcessInteractor interactor = sandbox.getProcessInteractor();
            interactor.sendCommand(commandToSend);

            sender.sendMessage("Command sent to sandbox '" + sandboxName + "': " + commandToSend);
        } catch (Exception e) {
            sender.sendMessage("An error occurred while sending the command: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}