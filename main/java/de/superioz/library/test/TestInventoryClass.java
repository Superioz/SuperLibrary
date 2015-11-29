package de.superioz.library.test;

import de.superioz.library.minecraft.server.common.inventory.InventoryContent;
import de.superioz.library.minecraft.server.common.inventory.RowPosition;
import de.superioz.library.minecraft.server.common.item.InteractableSimpleItem;
import de.superioz.library.minecraft.server.common.item.SimpleItem;
import de.superioz.library.minecraft.server.event.WrappedInventoryClickEvent;
import org.bukkit.DyeColor;
import org.bukkit.Material;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestInventoryClass {

    @InventoryContent
    private InteractableSimpleItem item1 = new InteractableSimpleItem(RowPosition.CENTER.getSlot(),
            new SimpleItem(Material.DIAMOND),
            WrappedInventoryClickEvent::cancelEvent);

    @InventoryContent(fill = true)
    private InteractableSimpleItem item2 = new InteractableSimpleItem(2,
            new SimpleItem(Material.STAINED_GLASS_PANE).setColor(DyeColor.BLACK).setAmount(0),
            WrappedInventoryClickEvent::cancelEvent);

}
