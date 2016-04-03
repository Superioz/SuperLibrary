package de.superioz.library.bukkit.common.command;

import de.superioz.library.java.util.list.ListUtil;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
@Getter
public class CommandWrapper {

	private String label, description, permission, usage, parentCommand;
	private List<String> aliases;
	private int min, max;
	private BukkitCommand command;
	private BukkitCommandExecutor commandExecutor;
	private Class<?> tabCompleterClass;
	private AllowedCommandSender allowedSender;
	private CommandType commandType;
	private List<String> flags;
	private Method parentMethod;
	private Class parentClass;

	private CommandWrapper parent;
	private List<CommandWrapper> subCommands;

	public CommandWrapper(Method method, CommandType type){
		this.parentMethod = method;
		this.commandType = type;
		this.parentClass = method.getDeclaringClass();
		this.commandExecutor = new BukkitCommandExecutor(this, parentClass);

		// If the command is a nested command
		if(getCommandType() == CommandType.NESTED){
			Command.Nested annotation = this.parentMethod.getAnnotation(Command.Nested.class);
			this.parentCommand = annotation.parent();
		}

		// If it's a normal command (and a root)
		Command annotation = this.parentMethod.getAnnotation(Command.class);
		if(getCommandType() == CommandType.ROOT){
			annotation = (Command) getParentClass().getAnnotation(Command.class);
			this.tabCompleterClass = annotation.tabCompleter();
		}

		// Main command things
		this.label = annotation.label();
		this.description = annotation.desc();
		this.usage = annotation.usage();
		this.aliases = Arrays.asList(annotation.aliases());
		this.min = annotation.min();
		this.max = annotation.max();
		this.allowedSender = annotation.commandTarget();
		this.tabCompleterClass = annotation.tabCompleter();
		this.permission = annotation.permission();
		this.flags = Arrays.asList(annotation.flags());

		// Init command
		this.initBukkitCommand();
	}

	/**
	 * Inits the parent
	 *
	 * @param parent The parent
	 * @return This
	 */
	public CommandWrapper initParent(CommandWrapper parent){
		this.parent = parent;
		return this;
	}

	/**
	 * Inits the subCommand
	 *
	 * @param subCommands The subCommands
	 * @return This
	 */
	public CommandWrapper initChildrens(List<CommandWrapper> subCommands){
		this.subCommands = subCommands;
		return this;
	}

	/**
	 * Init this command
	 */
	private void initBukkitCommand(){
		this.command = new BukkitCommand(this, this.label);
		this.command.setExecutor(this.commandExecutor);
		this.command.setTabCompleter(this.tabCompleterClass, this);

		if(!(this.aliases == null)){
			this.command.setAliases(this.aliases);
		}

		this.command.setUsage("/" + this.label);

		if(!this.permission.isEmpty())
			this.command.setPermission(this.permission);

		if(!this.description.isEmpty())
			this.command.setDescription(this.description);
	}

	/**
	 * Get subcommand with given label
	 *
	 * @param label The label
	 * @return The command
	 */
	public CommandWrapper getSubCommand(String label){
		if(getSubCommands() == null)
			return null;

		for(CommandWrapper cw : getSubCommands()){
			if(cw.getLabel().equalsIgnoreCase(label)
					|| ListUtil.listContains(cw.getAliases().toArray(), label))
				return cw;

			if(cw.hasSubCommand(label)){
				cw.getSubCommand(label);
			}
		}
		return null;
	}

	// -- Intern methods

	public boolean hasSubCommand(String label){
		return getSubCommand(label) != null;
	}

	public boolean hasSubCommands(){
		return getSubCommands() != null && !getSubCommands().isEmpty();
	}

	public boolean hasParent(){
		return getParent() != null;
	}

}
