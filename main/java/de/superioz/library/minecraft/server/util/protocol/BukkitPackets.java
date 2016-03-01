package de.superioz.library.minecraft.server.util.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import de.superioz.library.java.util.ReflectionUtils;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.NametagManager;
import de.superioz.library.minecraft.server.common.particle.ParticleData;
import de.superioz.library.minecraft.server.common.particle.ParticleEffect;
import de.superioz.library.minecraft.server.common.particle.ParticleInformation;
import de.superioz.library.minecraft.server.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitPackets {

	public static final int SCOREBOARD_TEAM_CREATED = 0;
	public static final int SCOREBOARD_TEAM_REMOVED = 1;
	public static final int SCOREBOARD_TEAM_UPDATED = 2;
	public static final int SCOREBOARD_PLAYERS_ADDED = 3;
	public static final int SCOREBOARD_PLAYERS_REMOVED = 4;

	/**
	 * Gets the packet for adding/removing to a fake team
	 *
	 * @param team The fake team
	 * @param mode The mode
	 *
	 * @return The packet
	 */
	public static PacketContainer getNameTagPacket(NametagManager.TeamHandle team, int mode,
			HashMap<NametagManager.TeamHandle, List<String>> teams){
		PacketContainer packetc = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

		if(!teams.containsKey(team)){
			return packetc;
		}

		packetc.getStrings().write(0, team.getName());
		packetc.getStrings().write(1, team.getName());
		packetc.getStrings().write(2, team.getPrefix());
		packetc.getStrings().write(3, team.getSuffix());
		packetc.getSpecificModifier(Collection.class).write(0, teams.get(team));
		packetc.getIntegers().write(1, mode);

		return packetc;
	}

	/**
	 * Get Entity Teleport Packet
	 *
	 * @param uniqueId The entity id
	 * @param current  The current location
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityTeleportPacket(int uniqueId, Location current){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

		packet.getIntegers().write(0, uniqueId);
		packet.getIntegers().write(1, LocationUtil.toFixedPoint(current.getX()));
		packet.getIntegers().write(2, LocationUtil.toFixedPoint(current.getY()));
		packet.getIntegers().write(3, LocationUtil.toFixedPoint(current.getZ()));
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(current.getYaw()));
		packet.getBytes().write(1, (byte) LocationUtil.toAngle(current.getPitch()));

		return packet;
	}

	/**
	 * Get Entity Destroy Packet
	 *
	 * @param uniqueId The entity id
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityDestroyPacket(int uniqueId){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		packet.getIntegerArrays().write(0, new int[]{uniqueId});

		return packet;
	}

	/**
	 * Get Entity Meta Packet
	 *
	 * @param uniqueId The entity id
	 * @param objects  The meta objects
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityMetaPacket(int uniqueId, List<WrappedWatchableObject> objects){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);

		packet.getIntegers().write(0, uniqueId);
		packet.getWatchableCollectionModifier().write(0, objects);

		return packet;
	}

	/**
	 * Get Entity Attach Packet
	 *
	 * @param passengerId The passenger id
	 * @param vehicleId   The vehicle id
	 * @param leash       Leash?
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityAttachPacket(int passengerId, int vehicleId, boolean leash){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ATTACH_ENTITY);

		packet.getIntegers().write(0, passengerId);
		packet.getIntegers().write(2, vehicleId);
		packet.getIntegers().write(0, leash ? 1 : 0);

		return packet;
	}

	/**
	 * Get Entity Move Packet
	 *
	 * @param uniqueId    The entity id
	 * @param newLocation The new location
	 * @param current     The old location
	 * @param onGround    Is the entity on ground
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityMovePacket(int uniqueId, Location newLocation, Location current,
			boolean onGround){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.REL_ENTITY_MOVE);

		packet.getIntegers().write(0, uniqueId);
		packet.getBytes().write(0, (byte) LocationUtil.toFixedPoint(newLocation.getX() - current.getX()));
		packet.getBytes().write(1, (byte) LocationUtil.toFixedPoint(newLocation.getY() - current.getY()));
		packet.getBytes().write(2, (byte) LocationUtil.toFixedPoint(newLocation.getZ() - current.getZ()));
		packet.getBooleans().write(0, onGround);

		return packet;
	}

	/**
	 * Get Entity Move and Look Packet
	 *
	 * @param uniqueId    The entity id
	 * @param newLocation The new location
	 * @param current     The old location
	 * @param onGround    Is the entity on ground
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityMoveAndLookPacket(int uniqueId, Location newLocation, Location current,
			boolean onGround){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_MOVE_LOOK);

		packet.getIntegers().write(0, uniqueId);
		packet.getBytes().write(0, (byte) LocationUtil.toFixedPoint(newLocation.getX() - current.getX()));
		packet.getBytes().write(1, (byte) LocationUtil.toFixedPoint(newLocation.getY() - current.getY()));
		packet.getBytes().write(2, (byte) LocationUtil.toFixedPoint(newLocation.getZ() - current.getZ()));
		packet.getBytes().write(3, (byte) LocationUtil.toAngle(newLocation.getYaw()));
		packet.getBytes().write(4, (byte) LocationUtil.toAngle(newLocation.getPitch()));
		packet.getBooleans().write(0, onGround);

		return packet;
	}

	/**
	 * Get Entity Rotation Packet
	 *
	 * @param uniqueId The entity id
	 * @param yaw      Yaw of movement
	 * @param pitch    Pitch of movement
	 * @param onGround Is the entity on ground
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityRotationPacket(int uniqueId, double yaw, double pitch, boolean onGround){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server
				.ENTITY_LOOK);

		packet.getIntegers().write(0, uniqueId);
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(yaw));
		packet.getBytes().write(1, (byte) LocationUtil.toAngle(pitch));
		packet.getBooleans().write(0, onGround);

		return packet;
	}

	/**
	 * Get EntityHeadRotationPacket
	 *
	 * @param uniqueId The entity id
	 * @param headYaw  The head yaw as double
	 *
	 * @return The packet
	 */
	public static PacketContainer getEntityHeadRotationPacket(int uniqueId, double headYaw){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server
				.ENTITY_HEAD_ROTATION);

		packet.getIntegers().write(0, uniqueId);
		packet.getBytes().write(0, (byte) LocationUtil.toAngle(headYaw));

		return packet;
	}

	/**
	 * Get Tab List Packet
	 *
	 * @param profile The profile of player/npc
	 * @param action  The action (add/remove ..)
	 *
	 * @return The packet
	 */
	public static PacketContainer getTablistPacket(WrappedGameProfile profile, EnumWrappers.PlayerInfoAction action){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);

		packet.getPlayerInfoAction().write(0, action);
		packet.getPlayerInfoDataLists().write(0, Collections.singletonList(new PlayerInfoData(profile, 0,
				EnumWrappers.NativeGameMode.NOT_SET,
				WrappedChatComponent.fromText(profile.getName()))));

		return packet;
	}

	/**
	 * Get inventory packet
	 *
	 * @param uniqueId The id
	 * @param slot     The slot
	 * @param stack    The stack
	 *
	 * @return The packet
	 */
	public static PacketContainer getHumanInventoryPacket(int uniqueId, int slot, ItemStack stack){
		PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
		packet.getIntegers().write(0, uniqueId);
		packet.getIntegers().write(1, slot);
		packet.getItemModifier().write(0, stack);

		return packet;
	}

	/**
	 * Get particle effect data
	 *
	 * @param information The particle info's
	 *
	 * @return The packet
	 */
	public static Object getParticleEffectPacket(ParticleInformation information){
		Class<?> packetClass = CraftBukkitUtil.PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutWorldParticles");
		Class<?> enumParticle = CraftBukkitUtil.PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
		Constructor<?> constructor = ReflectionUtils.getConstructor(packetClass);
		try{
			Object packet = constructor.newInstance();

			ReflectionUtils.setField("a", enumParticle.getEnumConstants()[information.getEffect().getId()], packet);
			ReflectionUtils.setField("j", true, packet);

			ParticleData data = information.getData();
			if(data != null){
				int[] packetData = data.getPacketData();
				ReflectionUtils.setField("k", information.getEffect() == ParticleEffect.ICONCRACK ? packetData :
						new int[]{packetData[0] | (packetData[1] << 12)}, packet);
			}

			ReflectionUtils.setField("b", information.getX(), packet);
			ReflectionUtils.setField("c", information.getY(), packet);
			ReflectionUtils.setField("d", information.getZ(), packet);
			ReflectionUtils.setField("e", information.getOffsetX(), packet);
			ReflectionUtils.setField("f", information.getOffsetY(), packet);
			ReflectionUtils.setField("g", information.getOffsetZ(), packet);
			ReflectionUtils.setField("h", information.getSpeed(), packet);
			ReflectionUtils.setField("i", information.getAmount(), packet);

			return packet;
		}
		catch(InstantiationException | IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
		}
		return "";
	}

}
