package de.superioz.library.bukkit.common.npc;

import de.superioz.library.bukkit.common.protocol.EnumWrappers;
import de.superioz.library.bukkit.common.protocol.PacketType;
import de.superioz.library.bukkit.common.protocol.ProtocolUtil;
import de.superioz.library.bukkit.common.protocol.WrappedPacket;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inventory of a fake human protocol and it handle the metadata for it
 * On using dont forget to update the inventory
 */
@Getter
public class NPCHumanInventory {

	private Map<EnumWrappers.ItemSlot, ItemStack> map = new HashMap<>();

	/**
	 * Get itemstack with given slot
	 *
	 * @return The itemstack
	 */
	public ItemStack getSlot(EnumWrappers.ItemSlot slot){
		return map.get(slot);
	}

	/**
	 * Set given slot with given item
	 * @param slot The slot
	 * @param item The item
	 */
	public void setSlot(EnumWrappers.ItemSlot slot, ItemStack item){
		this.map.put(slot, item);
	}

	/**
	 * Create packet
	 * @param entityId The entity id
	 * @return The wrappedpacket list
	 */
	public List<WrappedPacket> createPackets(int entityId){
		List<WrappedPacket> packetList = new ArrayList<>();
		for(EnumWrappers.ItemSlot slot : EnumWrappers.ItemSlot.values()){
			ItemStack stack = this.getSlot(slot);

			WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
			packet.getIntegers().write(0, entityId);
			packet.getItemSlotModifier().write(0, slot);
			packet.getItemModifier().write(0, ProtocolUtil.asNMSCopy(stack));
			packetList.add(packet);
		}
		return packetList;
	}

}
