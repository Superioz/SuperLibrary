package de.superioz.library.minecraft.server.lab.fakemob.root.holo;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.lab.fakemob.data.entity.EntityMetadataHandler;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntityAppearence;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntitySettings;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntityType;
import de.superioz.library.minecraft.server.lab.fakemob.root.CraftFakeEntity;
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
public class CraftHologramPart extends CraftFakeEntity {

    protected String text;

    protected CraftHologramPart(Location location, String text){
        super(new FakeEntityAppearence(text, location, EntityType.ARMOR_STAND),
                new FakeEntitySettings(false, false, true), FakeEntityType.ENTITY);

        // Metadata
        this.text = text;
        this.metaData = EntityMetadataHandler.getHologramDefaults(this);
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

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
}
