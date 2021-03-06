package de.superioz.library.bukkit.common.item;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.java.util.Consumer;
import de.superioz.library.bukkit.common.inventory.SuperInventory;
import de.superioz.library.bukkit.event.WrappedInventoryClickEvent;
import de.superioz.library.bukkit.util.BukkitUtilities;
import de.superioz.library.bukkit.util.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This class was created as a part of BukkitLibrary
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
        this(index, item, inv, new Consumer<WrappedInventoryClickEvent>() {
            @Override
            public void accept(WrappedInventoryClickEvent event){
                event.cancelEvent();
            }
        });
    }

    public InteractableSimpleItem(int index, SimpleItem item){
        this(index, item, null, new Consumer<WrappedInventoryClickEvent>() {
            @Override
            public void accept(WrappedInventoryClickEvent event){
                event.cancelEvent();
            }
        });
    }

    public InteractableSimpleItem(int index, SimpleItem item, Consumer<WrappedInventoryClickEvent> event){
        this(index, item, null, event);
    }

    /**
     * Registers all listeners
     *
     * @return The item
     */
    public InteractableSimpleItem register(){
        BukkitLibrary.pluginManager().registerEvents(this, BukkitLibrary.plugin());
        return this;
    }

    /**
     * Set parent to given inventory
     *
     * @param inv The inventory
     *
     * @return The item
     */
    public InteractableSimpleItem setParent(SuperInventory inv){
        this.inv = inv;
        return this;
    }

    /**
     * What happens on inventory click
     *
     * @param event The event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(BukkitUtilities.compareInventory(event.getInventory(), getInventory().getInventory())){
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

    /**
     * What happens on inventory drag
     *
     * @param event The event
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event){
        if(BukkitUtilities.compareInventory(event.getInventory(), getInventory().getInventory())){
            event.setCancelled(true);
        }
    }

    // -- Intern methods

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

}
