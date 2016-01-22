package de.superioz.library.minecraft.server.exception;

import de.superioz.library.minecraft.server.common.inventory.SuperInventory;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class InventoryCreateException extends Exception {

    private SuperInventory parent;
    private Class<?> target;
    private Reason reason;

    public InventoryCreateException(SuperInventory parent, Class<?> target, Reason reason){
        this.parent = parent;
        this.target = target;
        this.reason = reason;
    }

    // -- Intern methods

    public SuperInventory getParent(){
        return parent;
    }

    public Class<?> getTarget(){
        return target;
    }

    public Reason getReason(){
        return reason;
    }

    public enum Reason {

        TARGET_CLASS_NOT_ALLOWED("This class is not allowed for a inventory wrapper!");

        private String string;

        Reason(String string){
            this.string = string;
        }

        public String getReason(){
            return this.string;
        }

    }

}
