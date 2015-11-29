package de.superioz.library.minecraft.server.command;

import de.superioz.library.java.util.ReflectionUtils;
import de.superioz.library.java.util.list.ListUtil;
import de.superioz.library.minecraft.server.command.context.CommandContext;
import de.superioz.library.minecraft.server.exception.CommandRegisterException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class CommandHandler {

    private static CommandMap commandMap;
    private static List<CommandWrapper> commands = new ArrayList<>();
    public static final String EXECUTE_METHOD_NAME = "execute";

    public static CommandMap getCommandMap(){
        if(commandMap == null){
            try{
                Field f = ReflectionUtils.getField(Bukkit.getServer().getClass(), "commandMap");
                commandMap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            return commandMap;
        }

        return getCommandMap();
    }

    public static boolean registerCommand(Class<?> commandClass) throws CommandRegisterException{
        if(!isCommandClass(commandClass)){
            throw new CommandRegisterException(CommandRegisterException.Reason.CLASS_NOT_COMMAND_CASE,
                    commandClass);
        }

        // Get parent command
        CommandWrapper parentCommand = getParentCommand(commandClass);
        parentCommand.initialize(getFullCommands(commandClass));

        // Get bukkit command
        CommandWrapper.BukkitCommand command = parentCommand.getBukkitCommand();

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

    private static List<CommandWrapper> getFullCommands(Class<?> c){
        List<CommandWrapper>[] commands = getCommands(c);
        List<CommandWrapper> subCommands = commands[0];
        List<CommandWrapper> fullCommands = new ArrayList<>();

        // Loop through all subCommands
        for(CommandWrapper command : subCommands){
            if(hasSubCommands(command)){
                command.initialize(getSubCommands(command));
            }

            fullCommands.add(command);
        }

        return fullCommands;
    }

    @SuppressWarnings("unchecked")
    private static List<CommandWrapper>[] getCommands(Class<?>... c){
        List<CommandWrapper> subCommands = new ArrayList<>();
        List<CommandWrapper> nestedCommands = new ArrayList<>();

        for(Class<?> cl : c){
            for(Method m : cl.getMethods()){
                if(m.getName().equals(EXECUTE_METHOD_NAME)
                        || !isCommandMethod(m))
                    continue;
                CommandWrapper command = new CommandWrapper(m);

                if(isNestedCommandMethod(m))
                    nestedCommands.add(command);
                else
                    subCommands.add(command);
            }
        }

        return new List[]{subCommands, nestedCommands};
    }

    private static CommandWrapper getParentCommand(Class<?> c) throws CommandRegisterException{
        return new CommandWrapper(getExecuteMethod(c));
    }

    private static List<CommandWrapper> getSubCommands(CommandWrapper command){
        List<CommandWrapper> nestedCommands = getCommands(command.parentClass)[1];

        return nestedCommands.stream().filter(c ->
                c.getParentCommand().equalsIgnoreCase(command.getLabel())).collect(Collectors.toList());
    }

    private static boolean hasSubCommands(CommandWrapper command){
        List<CommandWrapper> subCommands = getSubCommands(command);
        return subCommands != null && subCommands.size() != 0;
    }

    // ================================================================================================

    public static List<CommandWrapper> getCommands(){
        return commands;
    }

    public static List<CommandWrapper> getCommands(String label){
        return getCommands().stream().filter(wr
                -> wr.getLabel().equalsIgnoreCase(label)).collect(Collectors.toList());
    }

    public static CommandWrapper getCommand(String label){
        for(CommandWrapper wr : getCommands(label)){
            if(!wr.isChild()){
                return wr;
            }
        }
        return null;
    }

    private static boolean isCommandClass(Class<?> c){
        return c.isAnnotationPresent(Command.class)
                && ListUtil.listContains(c.getInterfaces(), CommandCase.class);
    }

    private static boolean isCommandMethod(Method m){
        return m.isAnnotationPresent(SubCommand.class)
                && ListUtil.listContains(m.getParameterTypes(), CommandContext.class);
    }

    private static boolean isNestedCommandMethod(Method m){
        return m.isAnnotationPresent(SubCommand.Nested.class)
                && ListUtil.listContains(m.getParameterTypes(), CommandContext.class);
    }

    private static boolean isTabCompleter(Class<?> c){
        return c.getSuperclass().equals(BukkitTabCompleter.class);
    }

    private static Method getExecuteMethod(Class<?> c) throws CommandRegisterException{
        try{
            return c.getDeclaredMethod(EXECUTE_METHOD_NAME, CommandContext.class);
        }catch(NoSuchMethodException e){
            throw new CommandRegisterException(CommandRegisterException.Reason
                    .CLASS_NOT_COMMAND_CASE, c);
        }
    }

}
