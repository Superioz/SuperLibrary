package de.superioz.library.minecraft.server.common.item;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.event.WrappedItemInteractEvent;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public abstract class SimpleItemTool implements Listener {

    protected SimpleItem item;
    protected Consumer<WrappedItemInteractEvent> consumer;
    protected boolean staticPlace = false;

    public SimpleItemTool(SimpleItem item, Consumer<WrappedItemInteractEvent> eventConsumer){
        this.consumer = eventConsumer;
        this.item = item;

        SuperLibrary.registerListener(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem() == null
                || event.getItem().getType() == Material.AIR
                || !item.compareWith(event.getItem())){
            return;
        }

        getConsumer().accept(new WrappedItemInteractEvent(event));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR
                || !item.compareWith(event.getCurrentItem())){
            return;
        }

        if(this.staticPlace){
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if(event.getCursor() == null
                || event.getCursor().getType() == Material.AIR
                || !item.compareWith(event.getCursor())){
            return;
        }

        if(this.staticPlace){
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack() == null
                || event.getItemDrop().getItemStack().getType() == Material.AIR
                || !item.compareWith(event.getItemDrop().getItemStack())){
            return;
        }

        if(this.staticPlace){
            event.setCancelled(true);
        }
    }

    // -- Intern methods

    public void setStaticPlace(boolean staticPlace){
        this.staticPlace = staticPlace;
    }

    public SimpleItem getItem() {
        return item;
    }

    public Consumer<WrappedItemInteractEvent> getConsumer() {
        return consumer;
    }

    public boolean isStaticPlace() {
        return staticPlace;
    }
}
