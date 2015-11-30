package de.superioz.library.minecraft.server.common.item;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.event.WrappedItemInteractEvent;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
@Getter
public abstract class SimpleItemTool implements Listener {

    protected SimpleItem item;
    protected Consumer<WrappedItemInteractEvent> consumer;

    public SimpleItemTool(SimpleItem item, Consumer<WrappedItemInteractEvent> eventConsumer){
        this.consumer = eventConsumer;
        this.item = item;

        SuperLibrary.registerListener(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem() == null
                || event.getItem().getType() == Material.AIR
                || !item.getWrappedStack().equals(event.getItem())){
            return;
        }

        getConsumer().accept(new WrappedItemInteractEvent(event));
    }

}
