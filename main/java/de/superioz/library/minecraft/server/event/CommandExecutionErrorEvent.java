package de.superioz.library.minecraft.server.event;

import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.common.command.context.TabCompleterContext;
import org.bukkit.event.*;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class CommandExecutionErrorEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Reason reason;
    private CommandContext context;
    private TabCompleterContext tContext;

    public CommandExecutionErrorEvent(Reason reason, CommandContext context){
        this.reason = reason;
        this.context = context;
    }

    public CommandExecutionErrorEvent(Reason reason, TabCompleterContext context){
        this.reason = reason;
        this.tContext = context;
    }

    // -- Intern methods

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Reason getReason(){
        return reason;
    }

    public CommandContext getCommandContext(){
        return context;
    }

    public TabCompleterContext getTabContext(){
        return tContext;
    }

    public boolean isTabError(){
        return getTabContext() != null;
    }

    public enum Reason {

        INVALID_COMMAND_USAGE("&cInvalid usage of this command!"),
        NOT_ALLOWED_COMMANDSENDER("&cYou're not allowed to do this!"),
        NO_PERMISSIONS("&cYou don't have permissions!");

        String message;

        Reason(String string){
            this.message = string;
        }

        public String getDefaultMessage(){
            return message;
        }
    }

}
