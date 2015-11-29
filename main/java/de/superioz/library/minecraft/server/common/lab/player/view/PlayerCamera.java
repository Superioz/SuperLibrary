package de.superioz.library.minecraft.server.common.lab.player.view;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PlayerCamera {

    public static void setSight(Player camera, Player spectator){
        ProtocolUtil.sendServerPacket(getPacket(camera.getEntityId()), spectator);
    }

    protected static PacketContainer getPacket(int cameraID){
        PacketContainer packet = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.CAMERA);
        packet.getIntegers().write(0, cameraID);

        return packet;
    }

}
