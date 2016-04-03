package de.superioz.library.bukkit.common.command.context;

import org.bukkit.command.CommandSender;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class TabCompleterContext {

    protected CommandSender sender;
    protected String label;
    protected String[] args;

    public TabCompleterContext(CommandSender sender, String label, String[] args){
        this.sender = sender;
        this.label = label;
        this.args = args;
    }

    public CommandSender getSender(){
        return sender;
    }

    public String getLabel(){
        return label;
    }

    public String[] getArgs(){
        return args;
    }
}
