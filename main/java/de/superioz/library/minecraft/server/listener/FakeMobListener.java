package de.superioz.library.minecraft.server.listener;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class FakeMobListener implements Listener {

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event){
		final Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLater(SuperLibrary.plugin(), new Runnable() {
			@Override
			public void run(){
				NPCRegistry.updatePlayerView(player);
			}
		}, 5L);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.getFrom().getWorld() == event.getTo().getWorld() &&
				event.getFrom().getBlockX() == event.getTo().getBlockX() &&
				event.getFrom().getBlockY() == event.getTo().getBlockY() &&
				event.getFrom().getBlockZ() == event.getTo().getBlockZ())
			return;

		// Get player and check if the player is dead
		Player player = event.getPlayer();
		if(player.getHealth() <= 0.0D) return;

		// Get the chunks
		Chunk oldChunk = event.getFrom().getChunk();
		Chunk newChunk = event.getTo().getChunk();

		// Player moves into a new chunk
		if(oldChunk.getWorld() != newChunk.getWorld()
				|| oldChunk.getX() != newChunk.getX() || oldChunk.getZ() != newChunk.getZ()){
			NPCRegistry.updatePlayerView(player);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		NPCRegistry.updatePlayerView(player);
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		NPCRegistry.updatePlayerView(player);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		final Player player = event.getPlayer();
		Bukkit.getScheduler().runTaskLater(SuperLibrary.plugin(), new Runnable() {
			@Override
			public void run(){
				NPCRegistry.updatePlayerView(player);
			}
		}, 5L);
	}

	@EventHandler
	public void onPluginUnload(PluginDisableEvent event){
		if(event.getPlugin() != SuperLibrary.plugin()){
			return;
		}

		NPCRegistry.unregisterAll();
	}

}
