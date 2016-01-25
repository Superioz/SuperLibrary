package de.superioz.library.minecraft.server.event;

import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class PlayerInteractNPCEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private CraftFakeEntity entity;
    private Action action;

    public PlayerInteractNPCEvent(Player player, CraftFakeEntity entity, Action action){
        this.player = player;
        this.entity = entity;
        this.action = action;
    }

    public Action getAction(){
        return action;
    }

    public Player getPlayer(){
        return player;
    }

    public CraftFakeEntity getEntity(){
        return entity;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
