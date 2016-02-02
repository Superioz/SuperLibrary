package de.superioz.library.minecraft.server.util.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.NametagManager;
import de.superioz.library.minecraft.server.common.ParticleEffect;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.protocol.lib.WrapperPlayServerAttachEntity;
import de.superioz.library.minecraft.server.util.protocol.lib.WrapperPlayServerPlayerInfo;
import de.superioz.library.minecraft.server.util.protocol.lib.WrapperPlayServerScoreboardTeam;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitPackets {

    /**
     * Gets the packet for adding/removing to a fake team
     *
     * @param team The fake team
     * @param mode The mode
     * @return The packet
     */
    public static PacketContainer getNameTagPacket(NametagManager.TeamHandle team, int mode,
                                                   HashMap<NametagManager.TeamHandle, List<String>> teams) {
        PacketContainer packetc = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam(packetc);

        if (!teams.containsKey(team))
            return packet.getHandle();

        packet.setPrefix(ChatUtil.colored(team.getPrefix()));
        packet.setSuffix(ChatUtil.colored(team.getSuffix()));
        packet.setMode(mode);
        packet.setName(team.getName());
        packet.setDisplayName(team.getName());
        packet.setPlayers(teams.get(team));

        return packet.getHandle();
    }

    /**
     * Get Entity Teleport Packet
     *
     * @param uniqueId The entity id
     * @param current  The current location
     * @return The packet
     */
    public static PacketContainer getEntityTeleportPacket(int uniqueId, Location current) {
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
     * @return The packet
     */
    public static PacketContainer getEntityDestroyPacket(int uniqueId) {
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntegerArrays().write(0, new int[]{uniqueId});

        return packet;
    }

    /**
     * Get Entity Meta Packet
     *
     * @param uniqueId The entity id
     * @param objects  The meta objects
     * @return The packet
     */
    public static PacketContainer getEntityMetaPacket(int uniqueId, List<WrappedWatchableObject> objects) {
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
     * @return The packet
     */
    public static PacketContainer getEntityAttachPacket(int passengerId, int vehicleId, boolean leash) {
        WrapperPlayServerAttachEntity packet = new WrapperPlayServerAttachEntity();

        packet.setEntityID(passengerId);
        packet.setVehicleId(vehicleId);
        packet.setLeash(leash);

        return packet.getHandle();
    }

    /**
     * Get Entity Move Packet
     *
     * @param uniqueId    The entity id
     * @param newLocation The new location
     * @param current     The old location
     * @param onGround    Is the entity on ground
     * @return The packet
     */
    public static PacketContainer getEntityMovePacket(int uniqueId, Location newLocation, Location current, boolean onGround) {
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
     * @return The packet
     */
    public static PacketContainer getEntityMoveAndLookPacket(int uniqueId, Location newLocation, Location current, boolean onGround) {
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
     * @return The packet
     */
    public static PacketContainer getEntityRotationPacket(int uniqueId, double yaw, double pitch, boolean onGround) {
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
     * @return The packet
     */
    public static PacketContainer getEntityHeadRotationPacket(int uniqueId, double headYaw) {
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
     * @return The packet
     */
    public static PacketContainer getTablistPacket(WrappedGameProfile profile, EnumWrappers.PlayerInfoAction action) {
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setAction(action);
        packet.setData(Collections.singletonList(new PlayerInfoData(profile, 0,
                EnumWrappers.NativeGameMode.NOT_SET,
                WrappedChatComponent.fromText(profile.getName()))));

        return packet.getHandle();
    }

    /**
     * Get inventory packet
     *
     * @param uniqueId The id
     * @param slot     The slot
     * @param stack    The stack
     * @return The packet
     */
    public static PacketContainer getHumanInventoryPacket(int uniqueId, int slot, ItemStack stack) {
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, uniqueId);
        packet.getIntegers().write(1, slot);
        packet.getItemModifier().write(0, stack);

        return packet;
    }

    /**
     * Get particle effect data
     *
     * @param effect  The effect
     * @param x       The x
     * @param y       The y
     * @param z       The z
     * @param offsetX The offset X
     * @param offsetY The offset Y
     * @param offsetZ The offset Z
     * @param data    The data
     * @param amount  The amount
     * @return The packet
     */
    public static PacketContainer getParticleEffectPacket(ParticleEffect effect, float x, float y, float z,
                                                          float offsetX, float offsetY, float offsetZ, float data, int amount) {
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getIntegers().write(0, effect.getId());
        packet.getBooleans().write(0, true);
        packet.getFloat().write(0, x);
        packet.getFloat().write(1, y);
        packet.getFloat().write(2, z);
        packet.getFloat().write(3, offsetX);
        packet.getFloat().write(4, offsetY);
        packet.getFloat().write(5, offsetZ);
        packet.getFloat().write(6, data);
        packet.getIntegers().write(0, amount);

        return packet;
    }

}
