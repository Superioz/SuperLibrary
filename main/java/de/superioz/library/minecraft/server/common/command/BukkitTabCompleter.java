package de.superioz.library.minecraft.server.common.command;

import de.superioz.library.minecraft.server.common.command.context.TabCompleterContext;
import de.superioz.library.minecraft.server.event.CommandExecutionErrorEvent;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * This class was created as a part of SuperLibrary
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

    public List tabComplete(CommandSender sender, String label, String[] args){
        TabCompleterContext completerContext = new TabCompleterContext(sender, label, args);

        if(!sender.hasPermission(command.getPermission())){
            de.superioz.library.main.SuperLibrary.callEvent(new CommandExecutionErrorEvent(CommandExecutionErrorEvent
                    .Reason.NO_PERMISSIONS, completerContext));
        }

        return this.onTabComplete(completerContext);
    }

    public abstract List<String> onTabComplete(TabCompleterContext context);

    public CommandWrapper getCommand(String label){
        return CommandHandler.getCommand(label);
    }

}
