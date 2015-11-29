package de.superioz.library.minecraft.server.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class WrappedInventoryClickEvent {

    protected InventoryClickEvent event;

    public WrappedInventoryClickEvent(InventoryClickEvent event){
        this.event = event;
    }

    public WrappedInventoryClickEvent cancelEvent(){
        event().setResult(Event.Result.DENY);
        event.setCancelled(true);
        return this;
    }

    public WrappedInventoryClickEvent closeInventory(){
        new BukkitRunnable() {
            @Override
            public void run(){
                getPlayer().closeInventory();
            }
        }.run();
        return this;
    }

    public ItemStack getItem(){
        return event.getCurrentItem();
    }

    public int getSlot(){
        return event.getSlot();
    }

    public Player getPlayer(){
        return (Player) event.getWhoClicked();
    }

    public ClickType getClickType(){
        return event.getClick();
    }

    public InventoryAction getInventoryAction(){
        return event.getAction();
    }

    public InventoryClickEvent event(){
        return event;
    }

}
