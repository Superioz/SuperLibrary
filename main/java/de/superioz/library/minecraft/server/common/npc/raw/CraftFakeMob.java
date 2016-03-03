package de.superioz.library.minecraft.server.common.npc.raw;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.npc.FakeMob;
import de.superioz.library.minecraft.server.common.npc.meta.EntityMetaHandler;
import de.superioz.library.minecraft.server.common.npc.meta.MobType;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntityAppearence;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntitySettings;
import de.superioz.library.minecraft.server.common.npc.meta.settings.FakeEntityType;
import de.superioz.library.minecraft.server.common.npc.EntityIDManager;
import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.protocol.ProtocolUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public abstract class CraftFakeMob extends CraftFakeEntity {

    protected ArrayList<FakeMob> attachedEntities = new ArrayList<>();

    protected CraftFakeMob(EntityAppearence appearence, EntitySettings settings){
        super(appearence, settings, FakeEntityType.MOB);
    }

    /**
     * Updates the type of the mob
     *
     * @param newType The new entity type
     * @param players The viewers
     */
    public void updateType(MobType newType, Player... players){
        if(newType == null || this.getEntityType() == newType.getType() || !newType.getType().isAlive()) return;

        this.despawn(players);
        this.appearence.setType(newType.getType());
        this.metaData = EntityMetaHandler.getEntityDefaults(this);
        this.id = EntityIDManager.reserveNew(this.id);
        this.spawn(players);
    }

    /**
     * Intern method
     *
     * @param p The viewers
     */
    @Override
    protected void sendSpawnPacket(Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getIntegers().write(1, this.getEntityId());
        packet.getIntegers().write(2, LocationUtil.toFixedPoint(this.getLocation().getX()));
        packet.getIntegers().write(3, LocationUtil.toFixedPoint(this.getLocation().getY() + 0.001D));
        packet.getIntegers().write(4, LocationUtil.toFixedPoint(this.getLocation().getZ()));

        packet.getBytes().write(0, (byte) LocationUtil.toAngle(this.getLocation().getYaw()));
        packet.getBytes().write(1, (byte) LocationUtil.toAngle(this.getLocation().getPitch()));
        packet.getBytes().write(2, (byte) LocationUtil.toAngle(this.getLocation().getYaw()));

        packet.getDataWatcherModifier().write(0, this.getMetaData());

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);

        // Update viewers
        super.setViewers(true, p);
        super.initFixedName(true, p);
    }

    /**
     * Intern method
     *
     * @param newLoc The new location
     * @param p      The viewers
     */
    protected void sendEntityMovePacket(Location newLoc, Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.REL_ENTITY_MOVE);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getBytes().write(0, (byte) LocationUtil.toFixedPoint(newLoc.getX() - getLocation().getX()));
        packet.getBytes().write(1, (byte) LocationUtil.toFixedPoint(newLoc.getY() - getLocation().getY()));
        packet.getBytes().write(2, (byte) LocationUtil.toFixedPoint(newLoc.getZ() - getLocation().getZ()));
        packet.getBooleans().write(0, this.isOnGround());

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);
    }

    /**
     * Intern method
     *
     * @param newLoc The new location
     * @param p      The viewers
     */
    @Override
    protected void sendMoveAndLookPacket(Location newLoc, Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_MOVE_LOOK);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getBytes().write(0, (byte) LocationUtil.toFixedPoint(newLoc.getX() - getLocation().getX()));
        packet.getBytes().write(1, (byte) LocationUtil.toFixedPoint(newLoc.getY() - getLocation().getY()));
        packet.getBytes().write(2, (byte) LocationUtil.toFixedPoint(newLoc.getZ() - getLocation().getZ()));
        packet.getBytes().write(3, (byte) LocationUtil.toAngle(newLoc.getYaw()));
        packet.getBytes().write(4, (byte) LocationUtil.toAngle(newLoc.getPitch()));
        packet.getBooleans().write(0, this.isOnGround());

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);
    }

    /**
     * Intern method
     *
     * @param yaw   The yaw
     * @param pitch The pitch
     * @param p     The viewers
     */
    @Override
    protected void sendRotationPacket(double yaw, double pitch, Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server
                .ENTITY_LOOK);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getBytes().write(0, (byte) LocationUtil.toAngle(yaw));
        packet.getBytes().write(1, (byte) LocationUtil.toAngle(pitch));
        packet.getBooleans().write(0, this.isOnGround());

        ProtocolUtil.sendServerPacket(packet, p);
    }

    /**
     * Intern method
     *
     * @param headYaw The head yaw
     * @param p       The viewers
     */
    @Override
    protected void sendHeadRotation(double headYaw, Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server
                .ENTITY_HEAD_ROTATION);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getBytes().write(0, (byte) LocationUtil.toAngle(headYaw));

        ProtocolUtil.sendServerPacket(packet, p);
    }

}
