package dev.jensderuiter.minecraft_imagery.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

abstract public class AbstractCommand implements CommandExecutor {

    protected CommandSender sender;
    protected Command command;
    protected String s;
    protected String[] args;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
            sender.sendMessage("You don't have permission to execute this command.");
            return true;
        }

        this.sender = sender;
        this.command = command;
        this.s = s;
        this.args = args;

        this.execute();

        return true;
    }

    public void execute() {}

    public String getPermission() {
        return null;
    }
}
