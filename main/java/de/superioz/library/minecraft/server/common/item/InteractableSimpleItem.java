package de.superioz.library.minecraft.server.common.item;

import de.superioz.library.minecraft.server.util.BukkitUtil;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.inventory.SuperInventory;
import de.superioz.library.minecraft.server.event.WrappedInventoryClickEvent;
import de.superioz.library.minecraft.server.util.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class InteractableSimpleItem implements Listener {

    protected int index;
    protected SimpleItem item;
    protected Consumer<WrappedInventoryClickEvent> eventConsumer;
    protected SuperInventory inv;

    public InteractableSimpleItem(int index, SimpleItem item, SuperInventory inv,
                                  Consumer<WrappedInventoryClickEvent> event){
        this.item = item;
        this.eventConsumer = event;
        this.index = index;
        this.inv = inv;

        if(this.inv != null)
            this.register();
    }

    public InteractableSimpleItem(int index, SimpleItem item, SuperInventory inv){
        this(index, item, inv, WrappedInventoryClickEvent::cancelEvent);
    }

    public InteractableSimpleItem(int index, SimpleItem item){
        this(index, item, null, WrappedInventoryClickEvent::cancelEvent);
    }

    public InteractableSimpleItem(int index, SimpleItem item, Consumer<WrappedInventoryClickEvent> event){
        this(index, item, null, event);
    }

    public InteractableSimpleItem register(){
        SuperLibrary.pluginManager().registerEvents(this, SuperLibrary.plugin());
        return this;
    }

    public ItemStack getStack(){
        return item.getWrappedStack();
    }

    public int getIndex(){
        return index;
    }

    public Consumer<WrappedInventoryClickEvent> getEventConsumer(){
        return eventConsumer;
    }

    public SuperInventory getInventory(){
        return inv;
    }

    public InteractableSimpleItem setParent(SuperInventory inv){
        this.inv = inv;
        return this;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(BukkitUtil.compareInventory(event.getInventory(), getInventory().getInventory())){
            if(event.getCurrentItem() == null
                    || this.getStack() == null
                    || event.getInventory().getViewers().size() == 0){
                event.setCancelled(true);
                return;
            }

            if(ItemUtil.compare(this.getStack(), event.getCurrentItem())){
                eventConsumer.accept(new WrappedInventoryClickEvent(event));
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event){
        if(BukkitUtil.compareInventory(event.getInventory(), getInventory().getInventory())){
            event.setCancelled(true);
        }
    }

}
