package de.superioz.library.minecraft.server.common.lab.fakemob.root;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.entity.EntityMetadataHandler;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.settings.FakeEntityAppearence;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.settings.FakeEntitySettings;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.settings.FakeEntityType;
import de.superioz.library.minecraft.server.common.lab.fakemob.defined.FakeMob;
import de.superioz.library.minecraft.server.common.lab.packet.EntityIDManager;
import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class CraftFakeMob extends CraftFakeEntity {

    protected ArrayList<FakeMob> attachedEntities = new ArrayList<>();

    protected CraftFakeMob(FakeEntityAppearence appearence, FakeEntitySettings settings){
        super(appearence, settings, FakeEntityType.MOB);
    }

    public void updateType(EntityType newType, Player... players){
        if (newType == null || this.getEntityType() == newType || !newType.isAlive()) return;

        this.despawn(players);
        this.appearence.setType(newType);
        this.metaData = EntityMetadataHandler.getEntityDefaults(this);
        this.id = EntityIDManager.reserveNew(this.id);
        this.spawn(players);
    }

    // ============================== PROTOCOL ================================

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
    }

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

    protected void sendRotationPacket(double yaw, double pitch, Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server
                .ENTITY_LOOK);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getBytes().write(0, (byte) LocationUtil.toAngle(yaw));
        packet.getBytes().write(1, (byte) LocationUtil.toAngle(pitch));
        packet.getBooleans().write(0, this.isOnGround());

        ProtocolUtil.sendServerPacket(packet, p);
    }

    protected void sendHeadRotation(double headYaw, Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server
                .ENTITY_HEAD_ROTATION);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getBytes().write(0, (byte) LocationUtil.toAngle(headYaw));

        ProtocolUtil.sendServerPacket(packet, p);
    }

}
