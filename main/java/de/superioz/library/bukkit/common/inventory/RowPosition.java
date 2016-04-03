package de.superioz.library.bukkit.common.inventory;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public enum RowPosition {

    LEFT(1),
    BESIDE_LEFT(2),
    DOUBLE_BESIDE_LEFT(3),
    LEFT_CENTER(4),
    CENTER(5),
    RIGHT_CENTER(6),
    DOUBLE_BESIDE_RIGHT(7),
    BESIDE_RIGHT(8),
    RIGHT(9);

    int v;

    RowPosition(int v){
        this.v = v;
    }

    public int getSlot(){
        return v;
    }
}
