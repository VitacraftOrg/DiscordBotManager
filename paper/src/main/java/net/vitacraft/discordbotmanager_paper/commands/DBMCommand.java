package net.vitacraft.discordbotmanager_paper.commands;

import net.vitacraft.discordbotmanager.commands.CommandInterpreter;
import net.vitacraft.discordbotmanager.sandbox.SandboxManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class DBMCommand extends CommandInterpreter implements CommandExecutor {
    public DBMCommand(SandboxManager sbm){
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
        /*
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
         */
    }
}