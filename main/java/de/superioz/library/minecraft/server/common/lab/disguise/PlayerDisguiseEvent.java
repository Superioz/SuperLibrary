package de.superioz.library.minecraft.server.common.lab.disguise;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PlayerDisguiseEvent extends Event {

    private Player who;
    private PlayerDisguise disguise;
    private static final HandlerList handlers = new HandlerList();

    public PlayerDisguiseEvent(Player who, PlayerDisguise disguise){
        this.who = who;
        this.disguise = disguise;
    }

    public PlayerDisguise getDisguise(){
        return disguise;
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
