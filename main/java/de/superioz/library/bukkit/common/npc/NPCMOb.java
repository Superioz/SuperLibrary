package de.superioz.library.bukkit.common.npc;

import de.superioz.library.bukkit.common.protocol.PacketType;
import de.superioz.library.bukkit.common.protocol.WrappedPacket;
import de.superioz.library.bukkit.util.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created on 01.04.2016.
 */
@Getter
public class NPCMob extends NPC {

	private EntityType type;
	private int entityTypeId;
	private boolean fixedAim;

	/**
	 * Constructor for npcmob
	 *
	 * @param type        The entity type
	 * @param location    The location
	 * @param displayName The displayname
	 * @param fixedAim    Should he looks after players?
	 */
	public NPCMob(EntityType type, Location location, String displayName, boolean fixedAim){
		super(location, displayName);
		this.type = type;
		this.entityTypeId = type.getTypeId();
		this.fixedAim = fixedAim;

		// Set default datawatcher objects
		NPCHandler.setDefaultWatchableObjects(getDataWatcher(), getType());
	}

	public NPCMob(EntityType type, Location location, String displayName){
		this(type, location, displayName, true);
	}

	/**
	 * Toggles the aim update
	 */
	public void toggleAimUpdate(){
		this.fixedAim = !fixedAim;
	}

	@Override
	public void spawn(Player... players){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

		packet.getIntegers().write(0, getEntityId());
		packet.getUniqueIdentifier().write(0, UUID.randomUUID());
		packet.getIntegers().write(1, getEntityTypeId());
		packet.getDoubles().write(0, getLocation().getX());
		packet.getDoubles().write(1, getLocation().getY() + 0.001D);
		packet.getDoubles().write(2, getLocation().getZ());
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(getLocation().getYaw()));
		packet.getBytes().write(1, (byte) LocationUtil.toAngle(getLocation().getPitch()));
		packet.getBytes().write(2, (byte) LocationUtil.toAngle(getLocation().getYaw()));
		packet.getDataWatcherModifier().write(0, getDataWatcher());
		packet.send(players);

		// Set register
		super.setViewers(true, players);
		super.register();
	}

}
