package de.superioz.library.bukkit.common;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.bukkit.common.npc.NPCMob;
import de.superioz.library.bukkit.common.protocol.ProtocolEntityMeta;
import de.superioz.library.bukkit.common.protocol.ProtocolEntityMetaValue;
import de.superioz.library.bukkit.util.LocationUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 02.04.2016.
 */
@Getter
public class Hologram {

	protected static final double DISTANCE = 0.25;
	protected List<String> lines;
	protected List<Part> parts;
	private boolean active;
	private Location location;

	public Hologram(Location loc, String... lines){
		this.lines = Arrays.asList(lines);
		this.parts = new ArrayList<>();
		this.active = false;
		this.location = LocationUtil.fix(loc.getBlock().getLocation());
	}

	/**
	 * Add a line to the existing hologram
	 *
	 * @param text The text
	 */
	public void add(String text){
		this.lines.add(text);
	}

	/**
	 * Resets the hologram
	 *
	 * @param text Also every line?
	 */
	public void reset(boolean text){
		this.parts.clear();

		if(text)
			this.lines.clear();
	}

	/**
	 * Resets the text
	 *
	 * @param newLines The new lines for the hologram
	 */
	public void resetText(String... newLines){
		this.lines = Arrays.asList(newLines);
	}

	/**
	 * Convert the hologram
	 */
	private void convert(){
		Location firstLine = this.location.clone().add(0, (this.lines.size() / 2) * DISTANCE, 0);

		for(String s : lines){
			this.parts.add(new Hologram.Part(firstLine.clone(), s));
			firstLine = firstLine.subtract(0, DISTANCE, 0);
		}
	}

	/**
	 * Shows this hologram for given players
	 *
	 * @param players The viewers
	 */
	public void show(Player... players){
		if(!isActive()){
			this.convert();

			for(Part holo : this.getParts()){
				holo.spawn(players);
			}

			this.active = true;
		}
	}

	/**
	 * Hide this hologram for given players
	 *
	 * @param players The viewers
	 */
	public void hide(Player... players){
		if(isActive()){
			for(Part holo : this.getParts()){
				holo.despawn(false, players);
			}

			this.active = false;
			this.reset(false);
		}
	}

	/**
	 * Updates the hologram for given players
	 *
	 * @param players The viewers
	 */
	public void update(final Player... players){
		if(isActive()){
			this.hide(players);

			Bukkit.getScheduler().runTaskLater(BukkitLibrary.plugin(), new Runnable() {
				@Override
				public void run(){
					show(players);
				}
			}, 1L);
		}
		else{
			this.show(players);
		}
	}


	/**
	 * Just one part of the hologram
	 */
	public static class Part extends NPCMob {

		public Part(Location location, String displayName){
			super(EntityType.ARMOR_STAND, location, displayName);

			setMetadata(ProtocolEntityMeta.ARMOR_STAND_STATE, ProtocolEntityMetaValue.ARMOR_STAND_IS_SMALL.getBitMask());
			setMetadata(ProtocolEntityMeta.ENTITY_STATE, ProtocolEntityMetaValue.ENTITY_INVISIBLE.getBitMask());
		}

		@Override
		public void spawn(Player... players){
			super.spawn(players);
		}
	}

}
