package de.superioz.library.minecraft.server.command.context;

import de.superioz.library.minecraft.server.command.CommandWrapper;
import org.bukkit.command.CommandSender;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class CommandContext {

    protected CommandSender sender;
    protected CommandWrapper commandWrapper;
    protected String[] args;

    public CommandContext(CommandSender sender, CommandWrapper commandWrapper, String[] args){
        this.sender = sender;
        this.commandWrapper = commandWrapper;
        this.args = args;
    }

    public CommandSender getSender(){
        return sender;
    }

    public CommandWrapper getCommandWrapper(){
        return commandWrapper;
    }

    public String[] getArguments(){
        return args;
    }

    public int getArgumentsLength(){
        return args.length;
    }

    public boolean allows(CommandWrapper.AllowedSender sender){
        return commandWrapper.getAllowedSender() == sender
                || commandWrapper.getAllowedSender() == CommandWrapper.AllowedSender.PLAYER_AND_CONSOLE;
    }

    public String getArgument(int index){
        if(commandWrapper.isChild())
            index += 1;
        else if(commandWrapper.isNested())
            index += 2;

        return getArguments()[index];
    }

}
