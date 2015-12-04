package de.superioz.library.minecraft.server.common.command;

import de.superioz.library.java.util.ReflectionUtils;
import de.superioz.library.java.util.list.ListUtil;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.exception.CommandRegisterException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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
        }
        else{
            return commandMap;
        }

        return getCommandMap();
    }

    public static boolean registerCommand(Class<?> commandClass, Class<?>... subClasses) throws
            CommandRegisterException{
        if(!isCommandClass(commandClass)){
            throw new CommandRegisterException(CommandRegisterException.Reason.CLASS_NOT_COMMAND_CASE,
                    commandClass);
        }

        // Get parent command
        CommandWrapper parentCommand = getParentCommand(commandClass);
        parentCommand.initialize(getFullCommands(commandClass, subClasses));
        initParents(parentCommand);

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

    private static List<CommandWrapper> getFullCommands(Class<?> commandClass, Class<?>... c){
        List<CommandWrapper>[] commands = getCommands(commandClass, c);
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
    private static List<CommandWrapper>[] getCommands(Class<?> commandClass,
                                                      Class<?>... commandArray){
        List<Class<?>> commands = new ArrayList<>();
        commands.add(commandClass);
        Collections.addAll(commands, commandArray);

        List<CommandWrapper> subCommands = new ArrayList<>();
        List<CommandWrapper> nestedCommands = new ArrayList<>();

        for(Class<?> cl : commands){
            for(Method m : cl.getMethods()){
                if(!m.isAnnotationPresent(SubCommand.class)
                        || !isCommandMethod(m))
                    continue;
                CommandWrapper command = new CommandWrapper(m, isNestedCommandMethod(m)
                        ? CommandType.NESTED : CommandType.SUB);

                if(isNestedCommandMethod(m)){
                    nestedCommands.add(command);
                }
                else{
                    subCommands.add(command);
                }
            }
        }

        return new List[]{subCommands, nestedCommands};
    }

    private static CommandWrapper getParentCommand(Class<?> c) throws CommandRegisterException{
        return new CommandWrapper(getExecuteMethod(c), CommandType.ROOT);
    }

    private static List<CommandWrapper> getSubCommands(CommandWrapper command){
        List<CommandWrapper> nestedCommands = getCommands(command.parentClass)[1];

        return nestedCommands.stream().filter(c ->
                c.getParentCommand().equalsIgnoreCase(command.getLabel())).collect(Collectors.toList());
    }

    private static void initParents(CommandWrapper parentCommand)
            throws CommandRegisterException{
        for(CommandWrapper c : parentCommand.getSubCommands()){
            c.setParent(parentCommand);

            for(CommandWrapper sc : c.getSubCommands()){
                sc.setParent(c);
            }
        }
    }

    private static boolean hasSubCommands(CommandWrapper command){
        List<CommandWrapper> subCommands = getSubCommands(command);
        return subCommands != null && subCommands.size() != 0;
    }

    // ================================================================================================

    public static List<CommandWrapper> getCommands(){
        return commands;
    }

    public static List<CommandWrapper> getAllCommands() {
        List<CommandWrapper> commands = new ArrayList<>();

        for(CommandWrapper c : getCommands()){
            commands.add(c);

            for(CommandWrapper sc : c.getSubCommands()){
                commands.add(sc);

                commands.addAll(sc.getSubCommands().stream().collect(Collectors.toList()));
            }
        }
        return commands;
    }

    public static List<CommandWrapper> getCommands(String label){
        return getCommands().stream().filter(wr
                -> wr.getLabel().equalsIgnoreCase(label)).collect(Collectors.toList());
    }

    public static CommandWrapper getCommand(String label){
        for(CommandWrapper wr : getCommands(label)){
            if(wr.getCommandType() != CommandType.SUB){
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
        return m.isAnnotationPresent(SubCommand.Nested.class) && m.isAnnotationPresent(SubCommand.class)
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
