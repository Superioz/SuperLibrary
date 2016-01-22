package de.superioz.library.minecraft.server.lab.disguise;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PlayerInteractDisguisedPlayerEvent extends Event {

    private Player whoClicked;
    private Player disguied;
    private static final HandlerList handlers = new HandlerList();

    public PlayerInteractDisguisedPlayerEvent(Player who, Player disguised){
        this.whoClicked = who;
        this.disguied = disguised;
    }

    public Player getClicker(){
        return whoClicked;
    }

    public Player getClicked(){
        return disguied;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
