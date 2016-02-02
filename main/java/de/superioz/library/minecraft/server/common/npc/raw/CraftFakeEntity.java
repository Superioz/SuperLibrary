package de.superioz.library.minecraft.server.common.npc.raw;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import de.superioz.library.minecraft.server.common.npc.EntityIDManager;
import de.superioz.library.minecraft.server.common.npc.Hologram;
import de.superioz.library.minecraft.server.common.npc.NPCRegistry;
import de.superioz.library.minecraft.server.common.npc.meta.entity.EntityMeta;
import de.superioz.library.minecraft.server.common.npc.meta.entity.EntityMetaHandler;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntityAppearence;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntitySettings;
import de.superioz.library.minecraft.server.common.npc.meta.settings.FakeEntityType;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.protocol.BukkitPackets;
import de.superioz.library.minecraft.server.util.protocol.ProtocolUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */

public abstract class CraftFakeEntity {

    protected FakeEntityType type;
    protected EntityAppearence appearence;
    protected EntitySettings settings;

    protected Integer id;
    protected WrappedDataWatcher metaData;
    protected List<UUID> viewers;
    protected Hologram fixedName;

    protected CraftFakeEntity(EntityAppearence appearence, EntitySettings settings, FakeEntityType type){
        this.appearence = appearence;
        this.id = EntityIDManager.reserve(1);
        this.settings = settings;
        this.type = type;
        this.metaData = EntityMetaHandler.getEntityDefaults(this);
        this.viewers = new ArrayList<>();

        // Registers the entity
        NPCRegistry.register(this);
    }

    /**
     * Set the location for the entity
     *
     * @param newLoc The location
     */
    protected void setLocation(Location newLoc){
        appearence.setLoc(newLoc);
    }

    /**
     * Gets nearby players
     *
     * @param radius The radius
     *
     * @return The players in range
     */
    public List<Player> getNearbyPlayers(double radius){
        return this.getLocation().getWorld().getPlayers().stream()
                .filter(player -> this.getLocation().distance(player.getLocation()) <= radius).collect(Collectors.toList());
    }

    /**
     * Set if the nametag should be shown
     *
     * @param flag The visibility flag
     */
    public void setNameShown(boolean flag){
        this.getSettings().setShowName(flag);

        if(getName() != null && !getName().isEmpty()
                && nameShown()){
            metaData.setObject(EntityMeta.LIVING_ENTITY_NAME.getIndex(),
                    ChatUtil.colored(getName()));
            metaData.setObject(EntityMeta.LIVING_ENTITY_SHOWNAME.getIndex(),
                    ProtocolUtil.toByte(nameShown()));
        }
    }

    /**
     * Set given metadata with given value
     *
     * @param meta  The meta data
     * @param value The value
     */
    protected void setMetaData(EntityMeta meta, Object value){
        this.setMetaObject(meta.getIndex(), value);
    }

    /**
     * Set meta object with given index
     *
     * @param index The index
     * @param obj   The object
     */
    protected void setMetaObject(int index, Object obj){
        this.metaData.setObject(index, obj);
    }

    /**
     * Set the name for the entity
     *
     * @param name The new name
     */
    public void setName(String name){
        if(name != null && name.length() > 32)
            appearence.setName(name.substring(0, 32));
        if(getName() != null && getName().isEmpty()){
            appearence.setName(null);
        }

        this.setMetaData(EntityMeta.LIVING_ENTITY_NAME, getName() == null ? "" : ChatUtil.colored(getName()));
    }

    /**
     * Update the metadata of this entity
     *
     * @param players The viewers
     */
    public void updateMetadata(Player... players){
        this.sendMetaPacket(players);
    }

    /**
     * Update the position of the entity
     *
     * @param players The viewers
     */
    public void updatePosition(Player... players){
        this.sendTeleportPacket(players);
    }

    /**
     * Update the entity
     *
     * @param players The viewers
     */
    public void update(Player... players){
        this.despawn(players);
        this.spawn(players);
    }

    /**
     * Teleport the entity to given location
     *
     * @param newLoc  The new location
     * @param players The viewers
     */
    public void teleport(Location newLoc, Player... players){
        this.setLocation(newLoc);
        this.sendTeleportPacket(players);
    }

    /**
     * Attach passenger to given vehicle
     *
     * @param vehicleID   The vehicle
     * @param passengerID The passenger
     * @param players     The viewers
     */
    public void attach(int vehicleID, int passengerID, Player... players){
        sendAttachPacket(vehicleID, passengerID, players);
    }

    /**
     * Spawn the npc for given players
     *
     * @param players The viewers
     */
    public void spawn(Player... players){
        this.sendSpawnPacket(players);
        settings.setExists(true);
    }

    /**
     * Init fixed name
     *
     * @param players The viewers
     */
    public void initFixedName(boolean show, Player... players){
        if(!getSettings().isFixedName())
            return;
        if(this.fixedName == null)
            this.fixedName = new Hologram(getLocation().clone().add(0, 1.25, 0), getName());

        if(show)
            this.fixedName.show(players);
        else
            this.fixedName.hide(players);
    }

    /**
     * Rotates the entity
     *
     * @param to      The vector
     * @param players The viewers
     */
    public void rotate(Vector to, Player... players){
        float rotation = LocationUtil.getLocalAngle(getLocation().toVector(), to);
        this.rotateHead(rotation, players);
    }

    /**
     * Despawn the npc for given players
     *
     * @param players The viewers
     */
    public void despawn(Player... players){
        this.sendDestroyPacket(players);
        settings.setExists(false);
    }

    /**
     * Move the entity to given location
     *
     * @param loc     The location
     * @param players The viewers
     */
    public void move(Location loc, Player... players){
        this.sendMovePacket(loc, players);
        this.setLocation(loc);
    }

    /**
     * Rotate the head
     *
     * @param headYaw The yaw of the head
     * @param players The viewers
     */
    public void rotateHead(double headYaw, Player... players){
        this.sendHeadRotation(headYaw, players);

        Location oldLoc = this.getLocation();
        oldLoc.setYaw((float) headYaw);
        this.setLocation(oldLoc);
    }

    /**
     * Moves the npc and rotates it towards given location
     *
     * @param newLoc  The location
     * @param players The viewers
     */
    public void moveAndRotate(Location newLoc, Player... players){
        this.sendMoveAndLookPacket(newLoc, players);
        this.setLocation(newLoc);
    }

    /**
     * Rotates the whole body
     *
     * @param yaw     The yaw
     * @param pitch   The pitch
     * @param players The viewers
     */
    public void rotate(double yaw, double pitch, Player... players){
        this.sendRotationPacket(yaw, pitch, players);

        Location oldLoc = this.getLocation();
        oldLoc.setYaw((float) yaw);
        oldLoc.setPitch((float) pitch);
        this.setLocation(oldLoc);
    }

    // Intern method
    protected abstract void sendSpawnPacket(Player... p);

    /**
     * Unregisters the entity
     */
    protected void unregister(){
        NPCRegistry.unregister(this, true);
    }

    /**
     * Intern method
     *
     * @param p The viewers
     */
    protected void sendDestroyPacket(Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityDestroyPacket(getUniqueId()), p);

        // Update viewers and name
        setViewers(false, p);
        initFixedName(false, p);
    }

    /**
     * Intern method
     *
     * @param p The viewers
     */
    protected void sendTeleportPacket(Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityTeleportPacket(getUniqueId(), getLocation()), p);
    }

    /**
     * Intern method
     *
     * @param p The viewers
     */
    protected void sendMetaPacket(Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityMetaPacket(getUniqueId(), getMetaData().getWatchableObjects()), p);
    }

    /**
     * Intern method
     *
     * @param vehicleID   The vehicle id
     * @param passengerID The passenger id
     * @param p           The viewers
     */
    protected void sendAttachPacket(int vehicleID, int passengerID, Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityAttachPacket(passengerID, vehicleID, true), p);
    }

    /**
     * Intern method
     *
     * @param newLoc The new location
     * @param p      The viewers
     */
    protected void sendMovePacket(Location newLoc, Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityMovePacket(getUniqueId(), newLoc, getLocation(), isOnGround()), p);
    }

    /**
     * Intern method
     *
     * @param newLoc The new location
     * @param p      The viewers
     */
    protected void sendMoveAndLookPacket(Location newLoc, Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityMoveAndLookPacket(getUniqueId(), newLoc, getLocation(), isOnGround()), p);
    }

    /**
     * Intern method
     *
     * @param yaw   The yaw
     * @param pitch The pitch
     * @param p     The viewers
     */
    protected void sendRotationPacket(double yaw, double pitch, Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityRotationPacket(getUniqueId(), yaw, pitch, isOnGround()), p);
    }

    /**
     * Intern method
     *
     * @param headYaw The head yaw
     * @param p       The viewers
     */
    protected void sendHeadRotation(double headYaw, Player... p){
        ProtocolUtil.sendServerPacket(BukkitPackets.getEntityHeadRotationPacket(getUniqueId(), headYaw), p);
    }

    /**
     * Remove or add player from viewer list
     *
     * @param player The player
     * @param flag   The flag
     */
    protected void setViewer(Player player, boolean flag){
        UUID uuid = player.getUniqueId();

        if(flag){
            if(!viewers.contains(uuid))
                viewers.add(uuid);
        }
        else{
            if(viewers.contains(uuid))
                viewers.remove(uuid);
        }
    }

    /**
     * Removes or adds players to list
     *
     * @param flag    The flag
     * @param players The players
     */
    protected void setViewers(boolean flag, Player... players){
        for(Player p : players){
            setViewer(p, flag);
        }
    }

    // -- Intern methods

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

    public EntitySettings getSettings(){
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

    public List<UUID> getViewers(){
        return viewers;
    }

    public void setViewers(List<UUID> viewers){
        this.viewers = viewers;
    }
}
