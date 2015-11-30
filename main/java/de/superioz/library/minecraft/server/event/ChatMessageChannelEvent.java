package de.superioz.library.minecraft.server.event;

import de.superioz.library.minecraft.server.message.ChatMessageChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class ChatMessageChannelEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private ChatMessageChannel channel;
    private String message;
    private ChatMessageChannel.Target target;
    private Player[] playerTargets;

    public ChatMessageChannelEvent(ChatMessageChannel channel, String message,
                                   ChatMessageChannel.Target target, Player... playerTargets){
        this.channel = channel;
        this.message = message;
        this.target = target;
        this.playerTargets = playerTargets;
    }

    public String getMessage(){
        return message;
    }

    public ChatMessageChannel getChannel(){
        return channel;
    }

    public ChatMessageChannel.Target getTarget(){
        return target;
    }

    public Player[] getPlayerTargets(){
        return playerTargets;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled(){
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b){
        this.isCancelled = b;
    }
}
