package de.superioz.library.bukkit.common.npc;

import de.superioz.library.bukkit.util.BukkitUtilities;
import de.superioz.library.bukkit.util.LocationUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 01.04.2016.
 */
public class NPCRegistry {

	private static List<NPC> npcRegistry = new ArrayList<>();

	// Get the npc registry
	public static List<NPC> getNpcRegistry(){
		return npcRegistry;
	}

	/**
	 * Registers given npc
	 *
	 * @param npc The npc
	 */
	public static void register(NPC npc){
		if(!isRegistered(npc))
			npcRegistry.add(npc);
	}

	/**
	 * Unregisters given npc
	 *
	 * @param npc The npc
	 */
	public static void unregister(NPC npc){
		if(isRegistered(npc)){
			npcRegistry.remove(npc);
			npc.despawn(true, npc.getViewerAsPlayer());
		}
	}

	/**
	 * Checks if given npc is registered
	 *
	 * @param npc The npc
	 * @return The result
	 */
	public static boolean isRegistered(NPC npc){
		return npcRegistry.contains(npc);
	}

	/**
	 * Unregisters all entities
	 */
	public static void unregisterAll(){
		for(NPC e : getNpcRegistry()){
			unregister(e);
		}
		npcRegistry = new ArrayList<>();
	}

	/**
	 * Gets all entities whose viewers contains given player
	 *
	 * @param player The player
	 *
	 * @return The list
	 */
	public static List<NPC> getEntities(Player player){
		List<NPC> entities = new ArrayList<>();

		for(NPC e : getNpcRegistry()){
			if(e.getViewers().contains(player.getUniqueId())){
				entities.add(e);
			}
		}

		return entities;
	}

	/**
	 * Updates all entities of given players
	 *
	 * @param players The players
	 */
	public static void updatePlayerView(Player... players){
		for(Player p : players){
			for(NPC e : getEntities(p)){
				double distance = p.getLocation().distance(e.getLocation());

				if(!LocationUtil.checkRoughRange(distance, BukkitUtilities.CHUNK_WIDTH*4, BukkitUtilities.CHUNK_WIDTH))
					return;
				e.update();
			}
		}
	}

	/**
	 * Gets the npc of given id
	 *
	 * @param id The id
	 *
	 * @return The protocol
	 */
	public static NPC getNPC(int id){
		for(NPC entity : getNpcRegistry()){
			if(entity.getEntityId() == id)
				return entity;
		}
		return null;
	}

}
