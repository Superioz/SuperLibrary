package de.superioz.library.minecraft.server.lab.fakemob.data;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory of a fake human entity and it handle the metadata for it
 * On using dont forget to update the inventory
 */
public class FakeHumanInventory {

    /**
     * Items of inventory where:
     * 0 = Item in Hand
     * 1 = Boots
     * 2 = Leggings
     * 3 = Chestplate
     * 4 = Helmet
     */
    private ItemStack[] items = new ItemStack[5];

    /**
     * @return Item in Hand {@link #items}
     */
    public ItemStack getItemInHand(){
        return this.items[0];
    }

    /**
     * @return Boots of armor {@link #items}
     */
    public ItemStack getBoots(){
        return this.items[1];
    }

    /**
     * @return Leggings of armor {@link #items}
     */
    public ItemStack getLeggings(){
        return this.items[2];
    }

    /**
     * @return Chestplate of armor {@link #items}
     */
    public ItemStack getChestPlate(){
        return this.items[3];
    }

    /**
     * @return Helmet of armor {@link #items}
     */
    public ItemStack getHelmet(){
        return this.items[4];
    }

    /**
     * @param item {@link ItemStack}
     */
    public void setItemInHand(ItemStack item){
        this.setSlot(0, item);
    }

    /**
     * @param item {@link ItemStack}
     */
    public void setBoots(ItemStack item){
        this.setSlot(1, item);
    }

    /**
     * @param item {@link ItemStack}
     */
    public void setLeggings(ItemStack item){
        this.setSlot(2, item);
    }

    /**
     * @param item {@link ItemStack}
     */
    public void setChestPlate(ItemStack item){
        this.setSlot(3, item);
    }

    /**
     * @param item {@link ItemStack}
     */
    public void setHelmet(ItemStack item){
        this.setSlot(4, item);
    }

    /**
     * Get specific slot of inventory {@link #items}
     *
     * @param slot The slot {@link #items}
     * @return The {@link ItemStack} from this slot
     */
    public ItemStack getSlot(int slot){
        if(slot < 0 || slot >= this.items.length){
            return null;
        }
        return this.items[slot];
    }

    /**
     * Sets an itemstack to specific slot
     *
     * @param slot The slot {@link #items}
     * @param item The {@link ItemStack} which goes there
     */
    public void setSlot(int slot, ItemStack item){
        if(item != null && item.getType() == Material.AIR){
            item = null;
        }
        if(slot < 0 || slot >= this.items.length){
            return;
        }
        this.items[slot] = item;
    }

    /**
     * Create {@link PacketContainer} for inventory
     *
     * @param entityId The entity id which has the inventory holder
     * @return A {@link ArrayList} of {@link PacketContainer}
     */
    public List<PacketContainer> createPackets(int entityId){
        List<PacketContainer> packetList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            ItemStack stack = this.getSlot(i);
            PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            packet.getIntegers().write(0, entityId);
            packet.getIntegers().write(1, i);
            packet.getItemModifier().write(0, stack);
            packetList.add(packet);
        }
        return packetList;
    }

    /**
     * Returns if the inventory is empty
     *
     * @return boolean if the inventory is empty
     */
    public boolean isEmpty(){
        for(ItemStack item : this.items){
            if(item != null && item.getType() != Material.AIR){
                return false;
            }
        }
        return true;
    }

    /**
     * Clones this inventory
     * @return {@link FakeHumanInventory}
     */
    @Override
    public FakeHumanInventory clone(){
        FakeHumanInventory inv = new FakeHumanInventory();
        for(int i = 0; i < 5; i++){
            inv.setSlot(i, this.getSlot(i));
        }
        return inv;
    }

}
