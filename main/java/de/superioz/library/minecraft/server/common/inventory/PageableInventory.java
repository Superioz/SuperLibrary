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

    /**
     * Calculate pages for the inventory
     *
     * @param calcOnly     If the inventory should just be updated
     * @param contentClick Event if a content is clicked
     */
    public void calculatePages(boolean calcOnly, Consumer<WrappedInventoryClickEvent> contentClick){
        this.contentClick = contentClick;
        for(int i = 1; i < pageableList.getTotalPages() + 1; i++){
            if(!calcOnly)
                pages.add(calculatePage(i));
            else
                calculatePage(i);
        }
    }

    /**
     * Update all pages
     */
    public void update(){
        this.calculatePages(true, contentClick);
        pages.forEach(new Consumer<SuperInventory>() {
            @Override
            public void accept(SuperInventory superInventory){
                superInventory.build();
            }
        });
    }

    /**
     * Calculate given page
     *
     * @param page The page index
     *
     * @return The page as inventory
     */
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
                inventory.set(new InteractableSimpleItem(i + 1, content.get(i), inventory, contentClick));
        }

        // Pageable list
        if(page < pageableList.getTotalPages()){
            final int finalPage = page;
            inventory.set(new InteractableSimpleItem(getSize().getSlots() - 3, nextPage, inventory, new Consumer<WrappedInventoryClickEvent>() {
                @Override
                public void accept(WrappedInventoryClickEvent event){
                    event.cancelEvent();
                    event.closeInventory();
                    event.getPlayer().openInventory(getPage(finalPage + 1).build());
                }
            }));
        }
        if(page != 1){
            final int finalPage = page;
            inventory.set(new InteractableSimpleItem(getSize().getSlots() - 5, lastPage, inventory, new Consumer<WrappedInventoryClickEvent>() {
                @Override
                public void accept(WrappedInventoryClickEvent event){
                    event.cancelEvent();
                    event.closeInventory();
                    event.getPlayer().openInventory(getPage(finalPage - 1).build());
                }
            }));
        }
        inventory.set(new InteractableSimpleItem(getSize().getSlots() - 4, middleItem, inventory, new Consumer<WrappedInventoryClickEvent>() {
            @Override
            public void accept(WrappedInventoryClickEvent event){
                event.cancelEvent();
            }
        }));

        return inventory;
    }

    /**
     * Gets page with given index
     *
     * @param index The index
     *
     * @return The page as inventory
     */
    public SuperInventory getPage(int index){
        if(index > pages.size() || pages.size() == 0)
            return null;

        return pages.get(index - 1);
    }

    /**
     * Opens this inventory for given player
     *
     * @param player The player
     */
    public void open(Player player){
        player.openInventory(getPage(1).build());
    }

    /**
     * Get all viewers
     *
     * @return The list of viewers
     */
    public List<HumanEntity> getViewers(){
        final List<HumanEntity> entities = new ArrayList<>();
        getPages().forEach(new Consumer<SuperInventory>() {
            @Override
            public void accept(SuperInventory superInventory){
                entities.addAll(superInventory.getViewers());
            }
        });
        return entities;
    }

    /**
     * Get all viewers of given page
     *
     * @param page The page index
     *
     * @return The list of viewers
     */
    public List<HumanEntity> getViewers(int page){
        return getPage(page).getViewers();
    }

    /**
     * Set given objects to inventory
     *
     * @param items The items
     */
    public void setObjects(List<SimpleItem> items){
        int oldMaxPages = pageableList.getTotalPages();
        this.pageableList.setObjects(items);
        int newMaxPages = pageableList.getTotalPages();

        if(!(newMaxPages < oldMaxPages))
            return;

        List<HumanEntity> toClose = new ArrayList<>();
        for(int i = newMaxPages + 1; i < oldMaxPages + 1; i++){
            toClose.addAll(getViewers(i));
        }
        toClose.forEach(new Consumer<HumanEntity>() {
            @Override
            public void accept(HumanEntity humanEntity){
                humanEntity.closeInventory();
            }
        });
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
