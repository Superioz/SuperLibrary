package de.superioz.library.minecraft.server.common.lab.fakemob.root;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.entity.EntityMetadata;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.entity.EntityMetadataHandler;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.settings.FakeEntityAppearence;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.settings.FakeEntitySettings;
import de.superioz.library.minecraft.server.common.lab.fakemob.data.settings.FakeEntityType;
import de.superioz.library.minecraft.server.common.lab.packet.EntityIDManager;
import de.superioz.library.minecraft.server.common.lab.packet.protocol.WrapperPlayServerAttachEntity;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */

public abstract class CraftFakeEntity {

    protected FakeEntityType type;
    protected FakeEntityAppearence appearence;
    protected FakeEntitySettings settings;

    protected Integer id;
    protected WrappedDataWatcher metaData;

    protected CraftFakeEntity(FakeEntityAppearence appearence, FakeEntitySettings settings, FakeEntityType type){
        this.appearence = appearence;
        this.id = EntityIDManager.reserve(1);
        this.settings = settings;
        this.type = type;
        this.metaData = EntityMetadataHandler.getEntityDefaults(this);
    }

    // ===================================== GETTER =====================================

    public FakeEntityType getNPCType(){
        return type;
    }

    public EntityType getEntityType(){
        return appearence.getType();
    }

    public String getName(){
        return appearence.getName();
    }

    public Location getLocation(){
        return appearence.getLoc().clone();
    }

    public FakeEntitySettings getSettings(){
        return settings;
    }

    public boolean nameShown(){
        return settings.isShowName();
    }

    public boolean isOnGround(){
        return settings.isOnGround();
    }

    public boolean exists(){
        return settings.isExists();
    }

    public int getUniqueId(){
        return id;
    }

    public int getEntityId(){
        return appearence.getType().getTypeId();
    }

    public WrappedDataWatcher getMetaData(){
        return metaData;
    }

    // ===================================== SETTER =====================================

    protected void setLocation(Location newLoc){
        appearence.setLoc(newLoc);
    }

    public void setNameShown(boolean flag){
        this.getSettings().setShowName(flag);

        if(getName() != null && !getName().isEmpty()
                && nameShown()){
            metaData.setObject(EntityMetadata.LIVING_ENTITY_NAME.getIndex(),
                    ChatUtil.colored(getName()));
            metaData.setObject(EntityMetadata.LIVING_ENTITY_SHOWNAME.getIndex(),
                    ProtocolUtil.toByte(nameShown()));
        }
    }

    protected void setMetaData(EntityMetadata meta, Object value){
        this.setMetaObject(meta.getIndex(), value);
    }

    protected void setMetaObject(int index, Object obj){
        this.metaData.setObject(index, obj);
    }

    public void setName(String name){
        if(name != null && name.length() > 32)
            appearence.setName(name.substring(0, 32));
        if(getName() != null && getName().isEmpty()){
            appearence.setName(null);
        }

        this.setMetaData(EntityMetadata.LIVING_ENTITY_NAME, getName() == null ? "" : ChatUtil.colored(getName()));
    }

    // ===================================== OTHER =====================================

    public void updateMetadata(Player... players){
        this.sendMetaPacket(players);
    }

    public void updatePosition(Player... players){
        this.sendTeleportPacket(players);
    }

    public void update(Player... players){
        this.despawn(players);
        this.spawn(players);
    }

    public void teleport(Location newLoc, Player... players){
        this.setLocation(newLoc);
        this.sendTeleportPacket(players);
    }

    public void attach(int vehicleID, int passengerID, Player... players){
        sendAttachPacket(vehicleID, passengerID, players);
    }

    public void spawn(Player... players){
        this.sendSpawnPacket(players);
        settings.setExists(true);
    }

    public void despawn(Player... players){
        this.sendDestroyPacket(players);
        settings.setExists(false);
    }

    public void move(Location loc, Player... players){
        this.sendMovePacket(loc, players);
        this.setLocation(loc);
    }

    public void rotateHead(double headYaw, Player... players){
        this.sendHeadRotation(headYaw, players);

        Location oldLoc = this.getLocation();
        oldLoc.setYaw((float) headYaw);
        this.setLocation(oldLoc);
    }

    public void moveAndRotate(Location newLoc, Player... players){
        this.sendMoveAndLookPacket(newLoc, players);
        this.setLocation(newLoc);
    }

    public void rotate(double yaw, double pitch, Player... players){
        this.sendRotationPacket(yaw, pitch, players);

        Location oldLoc = this.getLocation();
        oldLoc.setYaw((float) yaw);
        oldLoc.setPitch((float) pitch);
        this.setLocation(oldLoc);
    }

    // ===================================== PROTOCOL =====================================

    protected abstract void sendSpawnPacket(Player... p);

    protected void sendDestroyPacket(Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntegerArrays().write(0, new int[]{this.getUniqueId()});

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);
    }

    protected void sendTeleportPacket(Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getIntegers().write(1, LocationUtil.toFixedPoint(getLocation().getX()));
        packet.getIntegers().write(2, LocationUtil.toFixedPoint(getLocation().getY()));
        packet.getIntegers().write(3, LocationUtil.toFixedPoint(getLocation().getZ()));
        packet.getBytes().write(0, (byte) LocationUtil.toAngle(getLocation().getYaw()));
        packet.getBytes().write(1, (byte) LocationUtil.toAngle(getLocation().getPitch()));

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);
    }

    protected void sendMetaPacket(Player... p){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getWatchableCollectionModifier().write(0, this.getMetaData().getWatchableObjects());

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);
    }

    protected void sendAttachPacket(int vehicleID, int passengerID, Player... p){
        WrapperPlayServerAttachEntity packet = new WrapperPlayServerAttachEntity();

        packet.setEntityID(passengerID);
        packet.setVehicleId(vehicleID);
        packet.setLeash(true);

        // Send server packet
        ProtocolUtil.sendServerPacket(packet.getHandle(), p);
    }

    protected void sendMovePacket(Location newLoc, Player... p){
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
