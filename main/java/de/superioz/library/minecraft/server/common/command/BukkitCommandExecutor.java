package de.superioz.library.minecraft.server.common.command;

import de.superioz.library.java.util.list.ListUtil;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.event.CommandExecutionErrorEvent;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
@Getter
public class BukkitCommandExecutor {

    protected CommandWrapper commandWrapper;
    protected CommandWrapper command;
    protected Class executorClass;

    public BukkitCommandExecutor(CommandWrapper commandWrapper, Class clazz){
        this.commandWrapper = commandWrapper;
        this.executorClass = clazz;
    }

    /**
     * The on command method
     *
     * @param commandSender The sender
     * @param label         The command label
     * @param args          The args
     *
     * @return The result
     */
    public boolean onCommand(CommandSender commandSender, String label, String[] args){
        // Get command
        command = CommandHandler.getCommand(label);
        CommandContext context = new CommandContext(commandSender, command, args);

        // Check label
        if(label.equalsIgnoreCase(command.getLabel())
                || ListUtil.listContains(command.getAliases().toArray(), command.getLabel())){

            if(checkCommandContext(context, command)){
                executeCommand(context);
                return true;
            }
        }
        return false;
    }

    /**
     * Execute the command with given context
     *
     * @param context The context
     *
     * @return The result
     */
    private boolean executeCommand(CommandContext context){
        try{
            int length = context.getArgumentsLength();
            CommandWrapper command = commandWrapper;

            if(length >= 1){
                for(int i = 0; i < length; i++){
                    String arg = context.getArgument(i);

                    if(!command.hasSubCommand(arg)){
                        break;
                    }
                    command = command.getSubCommand(arg);
                    context.setCommand(command);

                    // Check command
                    if(!checkCommandContext(context, command)){
                        return false;
                    }
                }
            }

            Method m = (command.getCommandType() == CommandType.SUB
                    || command.getCommandType() == CommandType.NESTED)
                    ? command.getParentMethod() : getExecuteCommand(getCommandWrapper().getParentClass());
            assert m != null;

            // Check command
            if(!checkCommandContext(context, command)){
                return false;
            }

            if(Modifier.isStatic(m.getModifiers())){
                m.invoke(null, context);
            }
            else{
                m.invoke(m.getDeclaringClass().newInstance(), context);
            }

            return true;
        }catch(IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | InstantiationException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the execute method for given class
     *
     * @param clazz The class
     *
     * @return The method
     */
    private Method getExecuteCommand(Class<?> clazz){
        try{
            return clazz.getDeclaredMethod(CommandHandler.EXECUTE_METHOD_NAME, CommandContext.class);
        }catch(NoSuchMethodException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check given context for given command
     *
     * @param context The context
     * @param command The command
     *
     * @return The result
     */
    private boolean checkCommandContext(CommandContext context, CommandWrapper command){
        CommandSender commandSender = context.getSender();

        // Check arguments length
        if((context.getArgumentsLength() > command.getMax()
                && !(command.getMax() <= 0))
                || context.getArgumentsLength() < command.getMin()){

            // Fire event
            SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                    .Reason.INVALID_COMMAND_USAGE, context));
            return false;
        }

        // Check commandSender
        if(commandSender instanceof Player){

            // Does the context allows a player
            if(!context.allows(AllowedCommandSender.PLAYER)){
                SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                        .Reason.NOT_ALLOWED_COMMANDSENDER, context));
                return false;
            }

            // Player
            Player player = (Player) context.getSender();
            String permission = command.getPermission();

            if(permission != null
                    && !permission.isEmpty()){
                if(!player.hasPermission(permission)){

                    // Fire event
                    SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                            .Reason.NO_PERMISSIONS, context));
                    return false;
                }
            }

            return true;
        }
        else{

            // Does the context allows the console
            if(!context.allows(AllowedCommandSender.CONSOLE)){
                de.superioz.library.main.SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                        .Reason.NOT_ALLOWED_COMMANDSENDER, context));
                return false;
            }

            return true;
        }
    }

}
