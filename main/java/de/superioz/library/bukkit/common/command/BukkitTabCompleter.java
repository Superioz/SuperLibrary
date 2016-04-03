package de.superioz.library.bukkit.common.command;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.bukkit.common.command.context.TabCompleterContext;
import de.superioz.library.bukkit.event.CommandExecutionErrorEvent;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
@Getter
public abstract class BukkitTabCompleter {

    private CommandWrapper command;
    private Class<?> c;
    private Class<?> tabCompleterClass;

    public BukkitTabCompleter(CommandWrapper wrapper){
        this.command = wrapper;
        this.c = wrapper.getParentClass();
        this.tabCompleterClass = wrapper.getTabCompleterClass();
    }

    /**
     * Tab complete method
     *
     * @param sender The sender
     * @param label  The command label
     * @param args   The arguments
     *
     * @return The result as list
     */
    public List tabComplete(CommandSender sender, String label, String[] args){
        TabCompleterContext completerContext = new TabCompleterContext(sender, label, args);

        if(!sender.hasPermission(command.getPermission())){
            BukkitLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                    .Reason.NO_PERMISSIONS, completerContext));
        }

        return this.onTabComplete(completerContext);
    }

    // -- Intern methods

    public abstract List<String> onTabComplete(TabCompleterContext context);

    public CommandWrapper getCommand(String label){
        return CommandHandler.getCommand(label);
    }

}
