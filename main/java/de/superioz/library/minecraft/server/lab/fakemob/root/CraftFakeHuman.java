package de.superioz.library.minecraft.server.lab.fakemob.root;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.lab.fakemob.data.FakeHumanInventory;
import de.superioz.library.minecraft.server.lab.fakemob.data.FakeHumanProfile;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntityAppearence;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntitySettings;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntityType;
import de.superioz.library.minecraft.server.lab.packet.protocol.WrapperPlayServerPlayerInfo;
import de.superioz.library.minecraft.server.lab.player.session.GameProfileBuilder;
import de.superioz.library.minecraft.server.lab.player.session.UUIDFetcher;
import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class CraftFakeHuman extends CraftFakeEntity {

    protected UUID universallyUniqueId;
    protected FakeHumanInventory inventory = new FakeHumanInventory();
    protected Collection<WrappedSignedProperty> playerSkin;
    protected WrappedGameProfile profile;
    protected FakeHumanProfile humanProfile;

    protected Thread fetchingThread;

    protected CraftFakeHuman(FakeEntityAppearence appearence, FakeEntitySettings settings,
                             FakeHumanProfile humanProfile) {
        super(appearence, settings, FakeEntityType.HUMAN);
        this.humanProfile = humanProfile;

        this.fetchingThread = new Thread(new Runnable() {
            @Override
            public void run(){
                profile = initProfile();
                playerSkin = profile.getProperties().get(getName());
                universallyUniqueId = profile.getUUID();
            }
        });
        this.fetchingThread.start();
    }

    //============================================ GETTER ============================================

    public Thread getFetchingThread(){
        return fetchingThread;
    }

    public FakeHumanProfile getHumanProfile(){
        return humanProfile;
    }

    public FakeHumanInventory getInventory(){
        return inventory;
    }

    public Collection<WrappedSignedProperty> getPlayerSkin(){
        return playerSkin;
    }

    public WrappedGameProfile getProfile(){
        return profile;
    }

    public UUID getUniversallyUniqueId(){
        return universallyUniqueId;
    }

    //============================================ OTHER ============================================

    public void updateName(final Player... players){
        this.despawn(players);
        Bukkit.getScheduler().runTaskLater(SuperLibrary.plugin(), new Runnable() {
            @Override
            public void run(){
                spawn(players);
            }
        }, 5L);
    }

    public void updateEquipment(Player... players){
        this.sendInventoryPacket(players);
    }

    @Override
    public void move(Location loc, Player... players){
        this.sendMovePacket(loc, players);
        this.setLocation(loc);
    }

    //============================================ PROTOCOL ============================================

    protected WrappedGameProfile initProfile(){
        GameProfile profile;
        try{
            profile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(ChatColor.stripColor(humanProfile.getSkinName())));
            Field name = profile.getClass().getDeclaredField("name");
            name.setAccessible(true);
            name.set(profile, this.getName());
        }catch(Exception e){
            profile = new GameProfile(UUID.randomUUID(), this.getName());
        }
        return WrappedGameProfile.fromHandle(profile);
    }

    protected void addToTablist(Player... players){
        // Wrappers
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        packet.setData(Collections.singletonList(new PlayerInfoData(getProfile(), 0,
                EnumWrappers.NativeGameMode.NOT_SET,
                WrappedChatComponent.fromText(getName()))));

        ProtocolUtil.sendServerPacket(packet.getHandle(), players);
    }

    protected void removeFromTablist(Player... players){
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        packet.setData(Collections.singletonList(new PlayerInfoData(this.profile, 0,
                EnumWrappers.NativeGameMode.NOT_SET,
                WrappedChatComponent.fromText(profile.getName()))));

        ProtocolUtil.sendServerPacket(packet.getHandle(), players);
    }

    @Override
    protected void sendSpawnPacket(Player... p) {
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);

        // First add player to tablist
        // Wait for finishing thread
        try{
            this.fetchingThread.join();
        }catch(InterruptedException e){ e.printStackTrace(); }
        this.addToTablist(p);

        packet.getIntegers().write(0, this.getUniqueId());
        packet.getIntegers().write(1, LocationUtil.toFixedPoint(this.getLocation().getX()));
        packet.getIntegers().write(2, LocationUtil.toFixedPoint(this.getLocation().getY() + 0.001D));
        packet.getIntegers().write(3, LocationUtil.toFixedPoint(this.getLocation().getZ()));
        packet.getIntegers().write(4, 0);

        packet.getBytes().write(0, (byte) LocationUtil.toAngle(this.getLocation().getYaw()));
        packet.getBytes().write(1, (byte) LocationUtil.toAngle(this.getLocation().getPitch()));

        packet.getSpecificModifier(UUID.class).write(0, profile.getUUID());
        packet.getDataWatcherModifier().write(0, this.metaData);

        // Send server packet
        ProtocolUtil.sendServerPacket(packet, p);
        this.sendInventoryPacket(p);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void sendDestroyPacket(Player... players){
        this.removeFromTablist(players);
        super.sendDestroyPacket(players);
    }

    protected void sendInventoryPacket(Player... players){
        List<PacketContainer> packets = this.inventory.createPackets(this.getUniqueId());
        if (packets.isEmpty()) return;

        for (PacketContainer packet : packets)
            ProtocolUtil.sendServerPacket(packet, players);
    }

}
