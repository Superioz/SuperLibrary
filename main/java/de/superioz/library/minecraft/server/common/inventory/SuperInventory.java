package de.superioz.library.minecraft.server.common.inventory;

import de.superioz.library.minecraft.server.common.item.InteractableSimpleItem;
import de.superioz.library.minecraft.server.exception.InventoryCreateException;
import de.superioz.library.minecraft.server.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SuperInventory {

    private Inventory inventory;
    private HashMap<Integer, InteractableSimpleItem> content;
    private InventorySize size;
    private String title;

    public SuperInventory(String title, InventorySize size){
        this.size = size;
        this.title = title;
        this.content = new HashMap<>();

        this.inventory = Bukkit.createInventory(null,
                this.size.getSlots(), ChatUtil.colored(getTitle()));
    }

    private SuperInventory put(int index, InteractableSimpleItem item){
        getContent().put(index, item);
        return this;
    }

    public SuperInventory set(InteractableSimpleItem... items){
        this.setExact(1, items);
        return this;
    }

    public SuperInventory setExact(int row, InteractableSimpleItem... items){
        for(InteractableSimpleItem i : items){
            int index = i.getIndex() + ((row-1)*9);
            this.put(index-1, i);
        }
        return this;
    }

    public SuperInventory fill(int row, InteractableSimpleItem item){
        if(!(row > getSize().getRows())){
            for(int i = 1; i < 10; i++){
                int index = i + ((row-1)*9);
                this.put(index-1, item);
            }
        }

        return this;
    }

    public Inventory build(){
        for(int i : getContent().keySet()){
            if(i < 0 || i >= getSize().getSlots())
                continue;
            InteractableSimpleItem item = getContent().get(i);
            getInventory().setItem(i, item.getStack());
        }

        return getInventory();
    }

    public SuperInventory from(Class<?> inventoryClass) throws InventoryCreateException{
        if(inventoryClass.getConstructors().length > 1){
            throw new InventoryCreateException(this, inventoryClass,
                    InventoryCreateException.Reason.TARGET_CLASS_NOT_ALLOWED);
        }

        for(Field f : inventoryClass.getDeclaredFields()){
            if(!f.isAnnotationPresent(InventoryContent.class)
                    || f.getType() != InteractableSimpleItem.class){
                continue;
            }
            InventoryContent content = f.getAnnotation(InventoryContent.class);

            try{
                f.setAccessible(true);

                InteractableSimpleItem item = (InteractableSimpleItem) f.get(inventoryClass.newInstance());
                item.setParent(this).register();

                if(!content.fill())
                    this.set(item);
                else
                    this.fill(item.getIndex(), item);
            }catch(IllegalAccessException | InstantiationException e){
                e.printStackTrace();
            }
        }

        return this;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public HashMap<Integer, InteractableSimpleItem> getContent(){
        return content;
    }

    public InventorySize getSize(){
        return size;
    }

    public String getTitle(){
        return title;
    }

}