package de.superioz.library.minecraft.server.lab.disguise;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PlayerUndisguiseEvent extends Event {

    private Player who;
    private static final HandlerList handlers = new HandlerList();

    public PlayerUndisguiseEvent(Player who){
        this.who = who;
    }

    public Player getWho(){
        return who;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
