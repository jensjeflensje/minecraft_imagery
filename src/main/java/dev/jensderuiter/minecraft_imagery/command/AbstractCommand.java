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
        this.sender = sender;
        this.command = command;
        this.s = s;
        this.args = args;
        return true;
    }
}
