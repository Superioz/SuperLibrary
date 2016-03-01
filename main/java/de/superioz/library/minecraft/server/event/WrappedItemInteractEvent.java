package de.superioz.library.minecraft.server.event;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class WrappedItemInteractEvent {

    protected PlayerInteractEvent event;
    protected Action action;
    protected Block clickedBlock;
    protected ItemStack item;
    protected Player player;

    public WrappedItemInteractEvent(PlayerInteractEvent event){
        this.event = event;
        this.action = event.getAction();
        this.clickedBlock = event.getClickedBlock();
        this.item = event.getItem();
        this.player = event.getPlayer();
    }

    public PlayerInteractEvent getEvent() {
        return event;
    }

    public ItemStack getItem() {
        return item;
    }

    public Action getAction() {
        return action;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    public Player getPlayer() {
        return player;
    }
}
