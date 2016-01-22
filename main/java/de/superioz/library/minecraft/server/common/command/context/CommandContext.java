package de.superioz.library.minecraft.server.common.command.context;

import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.CommandType;
import de.superioz.library.minecraft.server.common.command.CommandWrapper;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

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

    /**
     * Set the command for this context
     * @param command The command
     */
    public void setCommand(CommandWrapper command){
        this.command = command;

        int startIndex = 0;
        if(getCommand().getCommandType() == CommandType.SUB
                || getCommand().getCommandType() == CommandType.NESTED){
            startIndex = 1;
        }

        this.args = Arrays.copyOfRange(args, startIndex, args.length);
    }

    /**
     * Checks if the command allows given sender
     * @param sender The sender
     * @return The result
     */
    public boolean allows(AllowedCommandSender sender){
        return getCommand().getAllowedSender() == sender
                || getCommand().getAllowedSender()
                == AllowedCommandSender.PLAYER_AND_CONSOLE;
    }

    /**
     * Returns the argument with given index
     * @param index The index
     * @return The argument as string
     */
    public String getArgument(int index){
        if((index-1) < 0
                || (index) > args.length)
            index = 1;

        return getArguments()[index-1];
    }

    // -- Intern methods

    public CommandSender getSender(){
        return sender;
    }

    public String[] getArguments(){
        return args;
    }

    public int getArgumentsLength(){
        return getArguments().length;
    }

    public CommandWrapper getRoot(){
        return root;
    }

    public CommandWrapper getParent(){
        return getCommand().getParent();
    }

    public CommandWrapper getCommand(){
        if(command == null)
            return getRoot();
        return command;
    }

}
