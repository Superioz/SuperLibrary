package de.superioz.library.bukkit.common.command;

import de.superioz.library.bukkit.message.BukkitChat;
import de.superioz.library.java.util.ReflectionUtils;
import de.superioz.library.java.util.list.ListUtil;
import de.superioz.library.bukkit.common.command.context.CommandContext;
import de.superioz.library.bukkit.exception.CommandRegisterException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class CommandHandler {

	private static CommandMap commandMap;
	private static List<CommandWrapper> commands = new ArrayList<>();
	public static final String EXECUTE_METHOD_NAME = "execute";

	/**
	 * Gets the commandMap with reflection
	 *
	 * @return The commandmap object
	 */
	public static CommandMap getCommandMap(){
		if(commandMap == null){
			try{
				Field f = ReflectionUtils.getField(Bukkit.getServer().getClass(), "commandMap");
				commandMap = (CommandMap) f.get(Bukkit.getServer());
				return getCommandMap();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			return commandMap;
		}

		return getCommandMap();
	}

	/**
	 * Registers given command class
	 *
	 * @param commandClass The class
	 * @param subClasses   All classes of all sub commands
	 * @return The registering result
	 * @throws CommandRegisterException
	 */
	public static boolean registerCommand(Class<?> commandClass, Class<?>... subClasses) throws
			CommandRegisterException{
		if(!isCommandClass(commandClass)){
			throw new CommandRegisterException(CommandRegisterException.Reason.CLASS_NOT_COMMAND_CASE,
					commandClass);
		}

		// Get all commands
		List<Class<?>> classes = new ArrayList<>();
		classes.add(commandClass);
		classes.addAll(Arrays.asList(subClasses));
		List<CommandWrapper> fullCommands = getMethodalCommands(classes);

		// Get parent command
		// And initialises the parent/childrens
		CommandWrapper parentCommand = getParentCommand(commandClass);
		parentCommand = initRelations(parentCommand, fullCommands);

		// Get bukkit command
		BukkitCommand command = parentCommand.getCommand();

		for(CommandWrapper c : commands){
			if(c.equals(parentCommand)
					&& c.getLabel().equalsIgnoreCase(command.getLabel())){
				throw new CommandRegisterException(CommandRegisterException.Reason.COMMAND_ALREADY_EXISTS,
						commandClass);
			}
		}

		getCommandMap().register(parentCommand.getLabel(),
				command);
		if(!commands.contains(parentCommand)){
			commands.add(parentCommand);
		}

		return true;
	}

	/**
	 * Get all commands from methods from a specific class
	 *
	 * @param clazzes The classes
	 * @return The list of commands
	 */
	private static List<CommandWrapper> getMethodalCommands(List<Class<?>> clazzes){
		List<CommandWrapper> commmands = new ArrayList<>();

		for(Class<?> c : clazzes){
			for(Method m : c.getMethods()){
				if(isCommandMethod(m)){
					CommandWrapper cmd = new CommandWrapper(m, getCommandType(m));

					if(getCommand(cmd.getLabel(), commmands) == null){
						commmands.add(cmd);
					}
				}
			}
		}

		return commmands;
	}

	/**
	 * Initialises the parent and childrens from given commands
	 * @param parentCommand The parent
	 * @param oldCommands All commands
	 * @return The list of commands with parent and childrens
	 */
	private static CommandWrapper initRelations(CommandWrapper parentCommand, List<CommandWrapper> oldCommands){
		List<CommandWrapper> newCommands = new ArrayList<>();

		// Init parent
		parentCommand.initParent(parentCommand);
		for(CommandWrapper c : oldCommands){
			if(c.getCommandType() == CommandType.SUB){
				c.initParent(parentCommand);
			}
			else if(c.getCommandType() == CommandType.NESTED){
				Command.Nested nested = c.getParentMethod().getAnnotation(Command.Nested.class);
				String label = nested.parent();
				CommandWrapper parent = getCommand(label, oldCommands);

				if(parent == null){
					continue;
				}

				// Init parent
				c.initParent(parent);
			}

			// Add to new commands
			newCommands.add(c);
		}

		// Init children
		for(CommandWrapper c : newCommands){
			c.initChildrens(getChildrens(c, newCommands));
		}
		parentCommand.initChildrens(getChildrens(parentCommand, newCommands));

		return parentCommand;
	}

	/**
	 * Return the command from list with given label
	 *
	 * @param label    The label
	 * @param commands The commands
	 * @return The command
	 */
	private static CommandWrapper getCommand(String label, List<CommandWrapper> commands){
		for(CommandWrapper cw : commands){
			if(cw.getLabel().equalsIgnoreCase(label)){
				return cw;
			}
		}
		return null;
	}

	/**
	 * Get the children from given wrapper
	 *
	 * @param wrapper  The wrapper
	 * @param commands The command list
	 * @return The list
	 */
	private static List<CommandWrapper> getChildrens(CommandWrapper wrapper, List<CommandWrapper> commands){
		List<CommandWrapper> wrappers = new ArrayList<>();

		for(CommandWrapper c : commands){
			if(c.getParent() == null)
				continue;
			if(c.getParent().getLabel().equalsIgnoreCase(wrapper.getLabel())){
				wrappers.add(c);
			}
		}

		return wrappers;
	}

	/**
	 * Gets the parent class of given class
	 *
	 * @param c The class
	 * @return The command
	 * @throws CommandRegisterException
	 */
	private static CommandWrapper getParentCommand(Class<?> c) throws CommandRegisterException{
		return new CommandWrapper(getExecuteMethod(c), CommandType.ROOT);
	}

	/**
	 * Get all commands exists in registerlist
	 *
	 * @return The list of commands
	 */
	public static List<CommandWrapper> getAllCommands(){
		List<CommandWrapper> allCommands = new ArrayList<>();
		List<CommandWrapper> current = new ArrayList<>();
		List<CommandWrapper> switchCurrent = new ArrayList<>();

		current.addAll(getCommands());
		// Loop through all commands
		while(!current.isEmpty()){
			for(CommandWrapper c : current){
				if(!c.hasSubCommands())
					continue;
				switchCurrent.addAll(c.getSubCommands());
			}

			// Add all commands to list
			// And add commands for new loop
			allCommands.addAll(current);
			current.clear();
			current.addAll(switchCurrent);
			switchCurrent.clear();
		}

		return allCommands;
	}

	/**
	 * Get commands of given label
	 *
	 * @param label The label
	 * @return The list of commands
	 */
	public static List<CommandWrapper> getCommands(String label){
		List<CommandWrapper> l = new ArrayList<>();
		for(CommandWrapper wrapper : getCommands()){
			if(wrapper.getLabel().equalsIgnoreCase(label)){
				l.add(wrapper);
			}
		}
		return l;
	}

	/**
	 * Get the command wrapper of given label
	 *
	 * @param label The label as string
	 * @return The command
	 */
	public static CommandWrapper getCommand(String label){
		for(CommandWrapper wr : getCommands(label)){
			if(wr.getCommandType() != CommandType.SUB){
				return wr;
			}
		}
		return null;
	}

	/**
	 * Get method for executing the command
	 *
	 * @param c The class
	 * @return The method
	 * @throws CommandRegisterException
	 */
	private static Method getExecuteMethod(Class<?> c) throws CommandRegisterException{
		try{
			return c.getDeclaredMethod(EXECUTE_METHOD_NAME, CommandContext.class);
		}
		catch(NoSuchMethodException e){
			throw new CommandRegisterException(CommandRegisterException.Reason
					.CLASS_NOT_COMMAND_CASE, c);
		}
	}

	/**
	 * Returns the specific command type for given command method
	 *
	 * @param m The method
	 * @return The result as type
	 */
	private static CommandType getCommandType(Method m){
		if(m.isAnnotationPresent(Command.class)){
			if(m.isAnnotationPresent(Command.Nested.class))
				return CommandType.NESTED;
			return CommandType.SUB;
		}
		return CommandType.UNKNOWN;
	}

	// -- Intern methods

	public static List<CommandWrapper> getCommands(){
		return commands;
	}

	private static boolean isCommandClass(Class<?> c){
		return c.isAnnotationPresent(Command.class)
				&& ListUtil.listContains(c.getInterfaces(), CommandCase.class);
	}

	private static boolean isCommandMethod(Method m){
		return m.isAnnotationPresent(Command.class);
	}

	private static boolean isNestedCommandMethod(Method m){
		return m.isAnnotationPresent(Command.Nested.class) && isCommandMethod(m);
	}

	private static boolean isTabCompleter(Class<?> c){
		return c.getSuperclass().equals(BukkitTabCompleter.class);
	}

}
