package de.superioz.library.minecraft.server.common.command;

import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitCommand extends org.bukkit.command.Command {

    private CommandWrapper commandWrapper;
    public BukkitCommandExecutor executor = null;
    public BukkitTabCompleter completer = null;

    protected BukkitCommand(CommandWrapper commandWrapper, String name){
        super(name);
        this.commandWrapper = commandWrapper;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings){
        if(this.executor != null){
            executor.onCommand(commandSender, commandWrapper.getLabel(), strings);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> tabComplete(CommandSender commandsender, String label, String[] args){
        if(completer == null)
            return new ArrayList<>();

        return completer.tabComplete(commandsender, label, args);
    }

    public void setExecutor(BukkitCommandExecutor executor){
        this.executor = executor;
    }

    public void setTabCompleter(Class<?> clazz, CommandWrapper wrapper){
        if(clazz == null || clazz == BukkitTabCompleter.class
                || clazz.getSuperclass() != BukkitTabCompleter.class){
            return;
        }

        try{
            this.completer = (BukkitTabCompleter) clazz.getConstructor(CommandWrapper.class)
                    .newInstance(wrapper);
        }catch(InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e){
            e.printStackTrace();
        }
    }

}
