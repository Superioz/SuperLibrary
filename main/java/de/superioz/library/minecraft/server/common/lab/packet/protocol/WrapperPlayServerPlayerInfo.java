package de.superioz.library.minecraft.server.common.lab.packet.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;

import java.util.List;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class WrapperPlayServerPlayerInfo extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO;

    public WrapperPlayServerPlayerInfo() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerPlayerInfo(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.PlayerInfoAction getAction() {
        return handle.getPlayerInfoAction().read(0);
    }

    public void setAction(EnumWrappers.PlayerInfoAction value) {
        handle.getPlayerInfoAction().write(0, value);
    }

    public List<PlayerInfoData> getData() {
        return handle.getPlayerInfoDataLists().read(0);
    }

    public void setData(List<PlayerInfoData> value) {
        handle.getPlayerInfoDataLists().write(0, value);
    }
}