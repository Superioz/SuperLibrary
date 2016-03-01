package de.superioz.library.minecraft.server.common.command.context;

import lombok.Getter;
import org.bukkit.command.CommandSender;

/**
 * This class was created as a part of SuperLibrary
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
