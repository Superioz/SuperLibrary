package de.superioz.library.minecraft.server.common.command.context;

import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.CommandType;
import de.superioz.library.minecraft.server.common.command.CommandWrapper;
import org.bukkit.command.CommandSender;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class CommandContext {

    protected CommandSender sender;
    protected CommandWrapper root;
    protected CommandWrapper command;
    protected String[] args;

    public CommandContext(CommandSender sender, CommandWrapper root, String[] args){
        this.sender = sender;
        this.root = root;
        this.args = args;
    }

    public CommandSender getSender(){
        return sender;
    }

    public String[] getArguments(){
        return args;
    }

    public int getArgumentsLength(){
        return args.length;
    }

    public CommandWrapper getRoot(){
        return root;
    }

    public CommandWrapper getCommand(){
        if(command == null)
            return getRoot();
        return command;
    }

    public void setCommand(CommandWrapper command){
        this.command = command;
    }

    public boolean allows(AllowedCommandSender sender){
        return getCommand().getAllowedSender() == sender
                || getCommand().getAllowedSender()
                == AllowedCommandSender.PLAYER_AND_CONSOLE;
    }

    // /test arg0 arg1 arg2 arg3
    public String getArgument(int index){
        if(getCommand().getCommandType() == CommandType.SUB)
            index += 0;
        else if(getCommand().getCommandType() == CommandType.NESTED)
            index += 1;

        return getArguments()[index];
    }

}
