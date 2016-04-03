package de.superioz.library.bukkit.common.npc;

import de.superioz.library.bukkit.common.protocol.*;
import de.superioz.library.bukkit.util.ChatUtil;
import de.superioz.library.bukkit.util.LocationUtil;
import de.superioz.library.java.util.Consumer;
import lombok.Getter;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created on 01.04.2016.
 */
@Getter
public abstract class NPC {

	protected List<UUID> viewers;
	protected int entityId;
	protected Location location;
	protected WrappedDataWatcher dataWatcher;

	protected boolean fixedAim;

	/**
	 * Constructor for simply npc
	 *
	 * @param location    The default location
	 * @param displayName The display name
	 */
	public NPC(Location location, String displayName, boolean fixedAim){
		this.location = location;
		this.viewers = new ArrayList<>();
		this.entityId = UniqueIdManager.reserveNew();
		this.fixedAim = fixedAim;

		this.dataWatcher = new WrappedDataWatcher();
		this.setMetadata(ProtocolEntityMeta.ENTITY_CUSTOM_NAME, ChatUtil.colored(displayName));
	}

	public NPC(Location location, String displayName){
		this(location, displayName, false);
	}

	/**
	 * Gets the display name
	 *
	 * @return The display name
	 */
	public String getDisplayName(){
		return (String) this.getMetadata(ProtocolEntityMeta.ENTITY_CUSTOM_NAME);
	}

	/**
	 * Checks if display name is shown
	 *
	 * @return The result
	 */
	public boolean isDisplayNameShown(){
		return (boolean) this.getMetadata(ProtocolEntityMeta.ENTITY_CUSTOM_NAME_VISIBLE);
	}

	/**
	 * Get all nearbys players
	 *
	 * @param radius The radius
	 * @return The list of players nearby
	 */
	public List<Player> getNearbyPlayers(double radius){
		List<Player> players = new ArrayList<>();

		for(Player player : this.getLocation().getWorld().getPlayers()){
			if(player.getLocation().distance(this.getLocation()) <= radius){
				players.add(player);
			}
		}

		return players;
	}

	/**
	 * Updates the entity
	 */
	public void update(){
		this.despawn(false, getViewerAsPlayer());
		this.spawn(getViewerAsPlayer());
	}

	/**
	 * Toggles the aimlock (look after player)
	 */
	public void toogleAimLock(){
		this.fixedAim = !this.fixedAim;
	}

	/**
	 * Registers the npc at the npc registry
	 */
	public void register(){
		NPCRegistry.register(this);
	}

	/**
	 * -- Packet methods --
	 */

	public abstract void spawn(Player... players);

	/**
	 * Despawns the npc for given players
	 *
	 * @param constant If it should stay despawned
	 * @param players  The viewer
	 */
	public void despawn(boolean constant, Player... players){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ENTITY_DESTROY);

		packet.get(int[].class).write(0, getEntityId());
		packet.send(players);

		if(constant)
			setViewers(false, players);
	}

	/**
	 * Updates the metadata of the datawatcher of this entity
	 */
	public void updateMetadata(){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ENTITY_METADATA);

		packet.getIntegers().write(0, getEntityId());
		packet.getWatchableObjectCollections().write(0, getDataWatcher().getWatchableObjects());
		packet.send(getViewerAsPlayer());
	}

	/**
	 * Sets a metadata type with given object
	 *
	 * @param metadata The metadata index
	 * @param object   The object
	 */
	public void setMetadata(ProtocolEntityMeta metadata, Object object){
		this.dataWatcher.set(metadata, object);
	}

	/**
	 * Gets a metadata from given index
	 *
	 * @param meta The index
	 * @return The result
	 */
	public Object getMetadata(ProtocolEntityMeta meta){
		return this.dataWatcher.get(meta);
	}

	/**
	 * Attach given entity as passenger
	 *
	 * @param passengerId The entity id of the passenger
	 */
	public void setPassenger(int passengerId){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ATTACH_ENTITY);

		packet.getIntegers().write(0, passengerId);
		packet.getIntegers().write(1, getEntityId());
		packet.send(getViewerAsPlayer());
	}

	/**
	 * Attach given entity as vehicle
	 *
	 * @param vehicleId The entity id of the vehicle
	 */
	public void setVehicle(int vehicleId){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ATTACH_ENTITY);

		packet.getIntegers().write(0, getEntityId());
		packet.getIntegers().write(1, vehicleId);
		packet.send(getViewerAsPlayer());
	}

	/**
	 * Teleports this entity to given location
	 *
	 * @param newLocation The new location
	 */
	public void teleport(Location newLocation){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ENTITY_TELEPORT);

		packet.getIntegers().write(0, getEntityId());
		packet.getIntegers().write(1, LocationUtil.toFixedPoint(newLocation.getX()));
		packet.getIntegers().write(2, LocationUtil.toFixedPoint(newLocation.getY()));
		packet.getIntegers().write(3, LocationUtil.toFixedPoint(newLocation.getZ()));
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(newLocation.getYaw()));
		packet.getBytes().write(1, (byte) LocationUtil.toAngle(newLocation.getPitch()));
		packet.send(getViewerAsPlayer());

		this.location = newLocation;
	}

	/**
	 * Moves this entity to given location
	 *
	 * @param newLocation The new location
	 */
	public void move(Location newLocation){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.REL_ENTITY_MOVE);

		packet.getIntegers().write(0, getEntityId());
		packet.getIntegers().write(1, LocationUtil.toFixedPoint(newLocation.getX() - getLocation().getX()));
		packet.getIntegers().write(2, LocationUtil.toFixedPoint(newLocation.getY() - getLocation().getY()));
		packet.getIntegers().write(3, LocationUtil.toFixedPoint(newLocation.getZ() - getLocation().getZ()));
		packet.getBooleans().write(0, true);
		packet.send(getViewerAsPlayer());

		this.location = newLocation;
	}

	/**
	 * Moves and rotates this entity to given location
	 *
	 * @param newLocation The new location
	 * @param onGround    If the entity moves on ground
	 */
	public void moveAndLook(Location newLocation, boolean onGround){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);

		packet.getIntegers().write(0, getEntityId());
		packet.getBytes().write(0, (byte) LocationUtil.toFixedPoint(newLocation.getX() - getLocation().getX()));
		packet.getBytes().write(1, (byte) LocationUtil.toFixedPoint(newLocation.getY() - getLocation().getY()));
		packet.getBytes().write(2, (byte) LocationUtil.toFixedPoint(newLocation.getZ() - getLocation().getZ()));
		packet.getBytes().write(3, (byte) LocationUtil.toAngle(newLocation.getYaw()));
		packet.getBytes().write(4, (byte) LocationUtil.toAngle(newLocation.getPitch()));
		packet.getBooleans().write(0, onGround);
		packet.send(getViewerAsPlayer());

		this.location = newLocation;
	}

	/**
	 * Rotates this entity to given values
	 *
	 * @param yaw      The vertical rotation
	 * @param pitch    The horizontal rotation
	 * @param onGround If the entity is on ground
	 */
	public void rotate(double yaw, double pitch, boolean onGround){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ENTITY_LOOK);
		this.location.setYaw((float) yaw);
		this.location.setPitch((float) pitch);

		packet.getIntegers().write(0, getEntityId());
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(yaw));
		packet.getBytes().write(1, (byte) LocationUtil.toAngle(pitch));
		packet.getBooleans().write(0, onGround);
		packet.send(getViewerAsPlayer());
	}

	/**
	 * Rotates the head of this entity to given yaw
	 *
	 * @param headYaw The vertical rotation
	 */
	public void rotateHead(float headYaw){
		WrappedPacket packet = new WrappedPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);

		packet.getIntegers().write(0, getEntityId());
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(headYaw));
		packet.send(getViewerAsPlayer());
	}

	public void rotate(Vector to){
		float rotation = LocationUtil.getLocalAngle(getLocation().toVector(), to);
		this.rotateHead(rotation);
	}

	/**
	 * Sets given player as viewer
	 *
	 * @param flag    Remove or add
	 * @param players The players
	 */
	public void setViewers(boolean flag, Player... players){
		for(Player p : players){
			if(flag){
				if(!isViewer(p))
					viewers.add(p.getUniqueId());
			}
			else{
				if(isViewer(p))
					viewers.remove(p.getUniqueId());
			}
		}
	}

	/**
	 * Checks if given player is a viewer
	 *
	 * @param player The player
	 * @return The result
	 */
	public boolean isViewer(Player player){
		return viewers.contains(player.getUniqueId());
	}

	/**
	 * Get all viewers as player (converting from uuid to player)
	 *
	 * @return The list of player
	 */
	public Player[] getViewerAsPlayer(){
		List<Player> list = new ArrayList<>();

		for(UUID id : getViewers()){
			list.add(Bukkit.getPlayer(id));
		}
		return list.toArray(new Player[]{});
	}

}
