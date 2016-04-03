package de.superioz.library.bukkit.util;

import de.superioz.library.bukkit.common.protocol.EnumWrappers;
import de.superioz.library.bukkit.common.protocol.PacketType;
import de.superioz.library.bukkit.common.protocol.WrappedChatComponent;
import de.superioz.library.bukkit.common.protocol.WrappedPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class BukkitUtilities {

	public static final int CHUNK_WIDTH = 16;

	/**
	 * Verify given username
	 *
	 * @param name The name
	 * @return The result
	 */
	public static boolean verifyUsername(String name){
		return (name != null)
				&& !(name.isEmpty())
				&& !(name.length() > 16);
	}

	/**
	 * Get protocol of given world and id
	 *
	 * @param world    The world
	 * @param entityID The id
	 * @return The protocol
	 */
	public static Entity getEntity(World world, int entityID){
		for(Entity e : world.getEntities()){
			if(e.getEntityId() == entityID){
				return e;
			}
		}
		return null;
	}

	/**
	 * Get all online players
	 *
	 * @return The array
	 */
	public static Player[] onlinePlayers(){
		Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
		return pl.toArray(new Player[pl.size()]);
	}

	/**
	 * Push away given protocol from loc
	 *
	 * @param e     The protocol
	 * @param from  The from location
	 * @param speed The speed
	 */
	public static void pushAwayEntity(Entity e, Location from, double speed){
		Vector unit = e.getLocation().toVector().subtract(from.toVector()).normalize();
		e.setVelocity(unit.multiply(speed));
	}

	/**
	 * Compares given inventories
	 *
	 * @param first  First inventory
	 * @param second Second inventory
	 * @return The result
	 */
	public static boolean compareInventory(Inventory first, Inventory second){
		if(first == null || second == null) return true;
		if(!first.getTitle().equals(second.getTitle())) return false;
		if(first.getType() != second.getType()) return false;
		ItemStack[] firstContents = first.getContents();
		ItemStack[] secondContents = second.getContents();
		if(firstContents.length != secondContents.length) return false;
		for(int i = 0; i < firstContents.length; i++){
			if(firstContents[i] == null || secondContents[i] == null){
				continue;
			}
			else if(!firstContents[i].isSimilar(secondContents[i])){
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if given inventory has content
	 *
	 * @param inventory The inventory
	 * @return The result
	 */
	public static boolean hasContent(PlayerInventory inventory){
		for(ItemStack item : inventory.getContents()){
			if(item != null
					&& item.getType() != Material.AIR){
				return true;
			}
		}
		for(ItemStack item : inventory.getArmorContents()){
			if(item != null
					&& item.getType() != Material.AIR){
				return true;
			}
		}
		return false;
	}

	/**
	 * Send the tab header and footer for given players
	 *
	 * @param header  The header
	 * @param footer  The footer
	 * @param players The players
	 */
	public static void sendTabHeaderFooter(String header, String footer, Player... players){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

		// Text
		if(header == null)
			header = "Header";
		if(footer == null)
			footer = "Footer";

		// Set components
		packet.getChatComponents().write(0, new WrappedChatComponent(ChatUtil.colored(header)));
		packet.getChatComponents().write(1, new WrappedChatComponent(ChatUtil.colored(footer)));

		// Send protocol
		packet.send(players);
	}

	/**
	 * Flashes the redscreen of given player
	 *
	 * @param redness The redness from 0-20
	 * @param player  The player
	 */
	public static void flashRedScreen(int redness, Player player){
		// Redness
		int value = redness;
		if(redness < 0 || redness > 20){
			value = 1;
		}
		int dist = -50000 * value + 1000000;

		// Out
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.WORLD_BORDER);
		packet.getWorldBorderActions().write(0, EnumWrappers.WorldBorderAction.INITIALIZE);
		packet.getIntegers()
				.write(0, 29999984)
				.write(1, 15)
				.write(2, dist);
		packet.getLongs().write(0, 0L);
		packet.getDoubles()
				.write(0, player.getLocation().getX())
				.write(1, player.getLocation().getZ())
				.write(2, 200000.0D)
				.write(3, 200000.0D);

		// Send protocol
		packet.send(player);
	}

	/**
	 * Set tab name of player
	 */
	public static void setTabName(Player player, String newName){
		player.setPlayerListName(ChatUtil.colored(newName));
	}

}
