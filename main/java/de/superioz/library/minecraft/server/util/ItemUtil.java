package de.superioz.library.minecraft.server.util;

import org.bukkit.inventory.ItemStack;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class ItemUtil {

    /**
     * Compares two items of equalitity
     *
     * @param item1 One item
     * @param item2 Second item
     *
     * @return The result
     */
    public static boolean compare(ItemStack item1, ItemStack item2){
        boolean hasMeta = item1.hasItemMeta() && item2.hasItemMeta();
        boolean sameType = item1.getType() == item2.getType();
        boolean sameDisplayname = false;
        boolean hasDisplayname = false;

        if(hasMeta){
            hasDisplayname = item1.getItemMeta().hasDisplayName()
                    && item2.getItemMeta().hasDisplayName();
        }

        if(hasDisplayname){
            sameDisplayname = item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());
        }

        if(hasMeta && hasDisplayname && !sameDisplayname)
            return false;

        return sameType;
    }

}
