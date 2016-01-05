package de.superioz.library.minecraft.server.common.inventory;

import de.superioz.library.java.util.list.PageableList;
import de.superioz.library.minecraft.server.common.item.InteractableSimpleItem;
import de.superioz.library.minecraft.server.common.item.SimpleItem;
import de.superioz.library.minecraft.server.event.WrappedInventoryClickEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class PageableInventory {

    private PageableList<SimpleItem> pageableList;
    private Consumer<WrappedInventoryClickEvent> contentClick;
    private SimpleItem nextPage;
    private SimpleItem lastPage;
    private SimpleItem middleItem;
    private String title;
    private InventorySize size;

    private List<SuperInventory> pages = new ArrayList<>();

    public PageableInventory(String title, InventorySize size, List<SimpleItem> objects,
                             SimpleItem middleItem, SimpleItem nextPage, SimpleItem lastPage){
        this.title = title;
        this.size = size;
        if(size == InventorySize.ONE_ROW)
            size = InventorySize.TWO_ROWS;
        this.pageableList = new PageableList<>(size.getSlots() - 9, objects);
        this.nextPage = nextPage;
        this.lastPage = lastPage;
        this.middleItem = middleItem;
    }

    public void calculatePages(boolean calcOnly, Consumer<WrappedInventoryClickEvent> contentClick){
        this.contentClick = contentClick;
        for(int i = 1; i < pageableList.getTotalPages()+1; i++){
            if(!calcOnly)
                pages.add(calculatePage(i));
            else
                calculatePage(i);
        }
    }

    public void update(){
        this.calculatePages(true, contentClick);
        pages.forEach(SuperInventory::build);
    }

    private SuperInventory calculatePage(int page){
        SuperInventory inventory = getPage(page);

        if(inventory == null)
            inventory = new SuperInventory(getTitle(), getSize());

        if(!this.pageableList.firstCheckPage(page)){
            page = 1;
        }

        List<SimpleItem> content = pageableList.calculatePage(page);

        for(int i = 0; i < content.size(); i++){
            SimpleItem item = content.get(i);

            if(item != null)
                inventory.set(new InteractableSimpleItem(i+1, content.get(i), inventory, contentClick));
        }

        // Pageable list
        if(page < pageableList.getTotalPages()){
            final int finalPage = page;
            inventory.set(new InteractableSimpleItem(getSize().getSlots()-3, nextPage, inventory, event -> {
                event.cancelEvent();
                event.closeInventory();
                event.getPlayer().openInventory(getPage(finalPage + 1).build());
            }));
        }
        if(page != 1){
            final int finalPage = page;
            inventory.set(new InteractableSimpleItem(getSize().getSlots() - 5, lastPage, inventory, event -> {
                event.cancelEvent();
                event.closeInventory();
                event.getPlayer().openInventory(getPage(finalPage - 1).build());
            }));
        }
        inventory.set(new InteractableSimpleItem(getSize().getSlots() - 4, middleItem, inventory, WrappedInventoryClickEvent::cancelEvent));

        return inventory;
    }

    public SuperInventory getPage(int index){
        if(index > pages.size() || pages.size() == 0)
            return null;

        return pages.get(index-1);
    }

    public void open(Player player){
        player.openInventory(getPage(1).build());
    }

    public List<HumanEntity> getViewers(){
        List<HumanEntity> entities = new ArrayList<>();
        getPages().forEach(superInventory -> entities.addAll(superInventory.getViewers()));
        return entities;
    }

    public List<HumanEntity> getViewers(int page){
        return getPage(page).getViewers();
    }

    public void setObjects(List<SimpleItem> items){
        int oldMaxPages = pageableList.getTotalPages();
        this.pageableList.setObjects(items);
        int newMaxPages = pageableList.getTotalPages();

        if(!(newMaxPages < oldMaxPages))
            return;

        List<HumanEntity> toClose = new ArrayList<>();
        for(int i = newMaxPages+1; i < oldMaxPages+1; i++){
            toClose.addAll(getViewers(i));
        }
        toClose.forEach(HumanEntity::closeInventory);
    }

    // -- Intern methods

    public List<SimpleItem> getObjects(){
        return pageableList.getObjects();
    }

    public String getTitle(){
        return title;
    }

    public InventorySize getSize(){
        return size;
    }

    public List<SuperInventory> getPages(){
        return pages;
    }

}
