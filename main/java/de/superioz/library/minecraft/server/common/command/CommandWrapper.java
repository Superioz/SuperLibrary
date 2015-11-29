package de.superioz.library.minecraft.server.common.command;

import de.superioz.library.java.util.list.ListUtil;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
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
    private List<CommandWrapper> subCommands = new ArrayList<>();
    private CommandType commandType;
    private CommandWrapper parent;

    protected Class parentClass;
    protected Method parentMethod;

    public CommandWrapper(Method method, CommandType type){
        this.parentMethod = method;
        this.parentClass = method.getDeclaringClass();
        this.commandType = type;
        this.commandExecutor = new BukkitCommandExecutor(this, parentClass);

        if(getCommandType() == CommandType.NESTED){
            SubCommand.Nested annotation = this.parentMethod.getAnnotation(SubCommand.Nested.class);
            this.parentCommand = annotation.parent();
        }

        if(getCommandType() == CommandType.SUB
                || getCommandType() == CommandType.NESTED){
            SubCommand annotation = this.parentMethod.getAnnotation(SubCommand.class);
            this.label = annotation.label();
            this.description = annotation.desc();
            this.usage = annotation.usage();
            this.aliases = Arrays.asList(annotation.aliases());
            this.min = annotation.min();
            this.max = annotation.max();
            this.allowedSender = annotation.commandTarget();
            this.permission = annotation.permission();
        }
        else if(getCommandType() == CommandType.ROOT){
            Command annotation = (Command) this.parentClass.getAnnotation(Command.class);
            this.label = annotation.label();
            this.description = annotation.desc();
            this.usage = annotation.usage();
            this.aliases = Arrays.asList(annotation.aliases());
            this.min = annotation.min();
            this.max = annotation.max();
            this.allowedSender = annotation.commandTarget();
            this.tabCompleterClass = annotation.tabCompleter();
            this.permission = annotation.permission();
        }

        // Init command
        this.initCommand();
    }

    private void initCommand(){
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

    public void initialize(List<CommandWrapper> subCommands){
        this.subCommands = subCommands;
    }

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

    public boolean hasSubCommand(String label){
        return getSubCommand(label) != null;
    }

    public void setParent(CommandWrapper parent){
        this.parent = parent;
    }

    public boolean hasParent(){
        return getParent() != null;
    }

}
