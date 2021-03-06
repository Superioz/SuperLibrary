package de.superioz.library.bukkit.listener;

import de.superioz.library.bukkit.event.CommandExecutionErrorEvent;
import de.superioz.library.bukkit.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class DefaultCommandListener implements Listener {

    /**
     * Only important for me tho
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(CommandExecutionErrorEvent event){
        String message = "unknown";

        switch(event.getReason()){
            case NO_PERMISSIONS:
                message = CommandExecutionErrorEvent.Reason.NO_PERMISSIONS.getDefaultMessage();
                break;
            case NOT_ALLOWED_COMMANDSENDER:
                message = CommandExecutionErrorEvent.Reason.NOT_ALLOWED_COMMANDSENDER.getDefaultMessage();
                break;
            case INVALID_COMMAND_USAGE:
                message = CommandExecutionErrorEvent.Reason.INVALID_COMMAND_USAGE.getDefaultMessage();
                break;
        }

        if(event.isTabError()){
            event.getTabContext().getSender().sendMessage(ChatUtil.colored(message));
        }
        else{
            event.getCommandContext().getSender().sendMessage(ChatUtil.colored(message));
        }
    }

}
