package de.superioz.library.minecraft.server.common.command.context;

import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.CommandFlag;
import de.superioz.library.minecraft.server.common.command.CommandType;
import de.superioz.library.minecraft.server.common.command.CommandWrapper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	 * Returns the command sender as player
	 *
	 * @return The player
	 */
	public Player getSenderAsPlayer(){
		return (Player) getSender();
	}

	/**
	 * Set the command for this context
	 *
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
	 *
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
	 *
	 * @param index The index
	 * @return The argument as string
	 */
	public String getArgument(int index){
		if((index - 1) < 0
				|| (index) > args.length)
			index = 1;

		return getArguments()[index - 1];
	}

	/**
	 * Gets specified command flag out of arguments
	 *
	 * @param specifier The name of the flag
	 * @param argValue  How many arguments need this flag?
	 * @return The flag
	 */
	public CommandFlag getCommandFlag(String specifier, int argValue){
		String[] arguments = getArguments();
		for(int i = 0; i < arguments.length; i++){
			String s = arguments[i];

			if(s.equalsIgnoreCase(CommandFlag.SPECIFIER + specifier)){
				CommandFlag flag = new CommandFlag(specifier, argValue > 0, argValue);
				flag.setArguments(Arrays.copyOfRange(arguments, i + 1, arguments.length));
				return flag;
			}
		}
		return null;
	}

	/**
	 * Gets specified command flags from arguments (without argument limit)
	 *
	 * @param specifier The specifier
	 * @return The flag
	 */
	public CommandFlag getCommandFlag(String specifier){
		return getCommandFlag(specifier, -1);
	}

	/**
	 * Check if command arguments contains at least one of the given flags
	 *
	 * @param similarSpecifier The specifier
	 * @return The result
	 */
	public boolean hasFlag(String... similarSpecifier){
		for(String s : similarSpecifier){
			if(getCommandFlag(s, -1) != null)
				return true;
		}
		return false;
	}

	/**
	 * Check if command arguments contains all given flags
	 *
	 * @param specifier The flag specifier
	 * @return The result
	 */
	public boolean hasFlags(String... specifier){
		for(String s : specifier){
			if(!hasFlag(s))
				return false;
		}
		return true;
	}

	/**
	 * Get all flags used in this context
	 *
	 * @return The list of flags
	 */
	public List<CommandFlag> getFlags(){
		List<CommandFlag> flagList = new ArrayList<>();

		for(String flag : getCommand().getFlags()){
			if(this.hasFlag(flag)){
				CommandFlag f = getCommandFlag(flag, -1);
				flagList.add(f);
			}
		}

		return flagList;
	}

	/**
	 * Get all flags as strings (specifier)
	 *
	 * @return The list of flags as string list
	 */
	public List<String> getRawFlags(){
		List<String> flags = new ArrayList<>();

		for(CommandFlag flag : getFlags()){
			flags.add(flag.getName());
		}

		return flags;
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
