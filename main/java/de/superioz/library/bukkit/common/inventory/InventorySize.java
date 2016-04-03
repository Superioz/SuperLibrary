package de.superioz.library.bukkit.common.inventory;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public enum InventorySize {

    ONE_ROW(1),
    TWO_ROWS(2),
    THREE_ROWS(3),
    FOUR_ROWS(4),
    FIVE_ROWS(5),
    SIX_ROWS(6);

    int v;

    InventorySize(int v){
        this.v = v;
    }

    public int getRows(){
        return v;
    }

    public int getSlots(){
        return 9 * v;
    }

}
