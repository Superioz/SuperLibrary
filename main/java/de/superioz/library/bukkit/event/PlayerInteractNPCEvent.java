package de.superioz.library.bukkit.event;

import de.superioz.library.bukkit.common.npc.NPC;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
@Getter
public class PlayerInteractNPCEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private NPC npc;
    private Action action;

    public PlayerInteractNPCEvent(Player player, NPC npc, Action action){
        this.player = player;
        this.npc = npc;
        this.action = action;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
