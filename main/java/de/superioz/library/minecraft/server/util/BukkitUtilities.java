package de.superioz.library.minecraft.server.util;

import de.superioz.library.minecraft.server.common.ParticleData;
import de.superioz.library.minecraft.server.common.ParticleEffect;
import de.superioz.library.minecraft.server.util.protocol.BukkitPackets;
import de.superioz.library.minecraft.server.util.protocol.CraftBukkitUtil;
import de.superioz.library.minecraft.server.util.protocol.ProtocolUtil;
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
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitUtilities {

	public static final int CHUNK_WIDTH = 16;

	/**
	 * Verify given username
	 *
	 * @param name The name
	 *
	 * @return The result
	 */
	public static boolean verifyUsername(String name){
		return (name != null)
				&& !(name.isEmpty())
				&& !(name.length() > 16);
	}

	/**
	 * Get entity of given world and id
	 *
	 * @param world    The world
	 * @param entityID The id
	 *
	 * @return The entity
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
	 * Push away given entity from loc
	 *
	 * @param e     The entity
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
	 *
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
	 * Shows particle to given players
	 *
	 * @param effect  The effect
	 * @param data    The data
	 * @param players The viewer
	 */
	public static void showParticle(ParticleEffect effect, ParticleData data, Player... players){
		CraftBukkitUtil.sendPacket(BukkitPackets.getParticleEffectPacket(effect,
				data.getX(), data.getY(), data.getZ(),
				data.getOffsetX(), data.getOffsetY(),
				data.getOffsetZ(),
				data.getData(), data.getAmount()), players);
	}

	/**
	 * Checks if given inventory has content
	 *
	 * @param inventory The inventory
	 *
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
	 * Set tab header footer for players
	 */
	public static void setTabHeaderFooter(String header, String footer, Player... players){
		CraftBukkitUtil.sendTabHeaderFooter(header, footer, players);
	}

	/**
	 * Set tab name of player
	 */
	public static void setTabName(Player player, String newName){
		player.setPlayerListName(ChatUtil.colored(newName));
	}

}
