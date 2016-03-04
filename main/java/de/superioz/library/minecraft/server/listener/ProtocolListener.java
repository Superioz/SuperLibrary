package de.superioz.library.minecraft.server.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.npc.NPCRegistry;
import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeEntity;
import de.superioz.library.minecraft.server.event.PlayerInteractNPCEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class ProtocolListener implements PacketListener {

    @Override
    public void onPacketSending(PacketEvent packetEvent){
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent){
        PacketContainer packet = packetEvent.getPacket();
        final Player player = packetEvent.getPlayer();

        if(packet.getType() == PacketType.Play.Client.USE_ENTITY){
            int id = packet.getIntegers().read(0);

            if(id < 0) return;
            final CraftFakeEntity entity = NPCRegistry.getNPC(id);
            if(entity == null || player.getWorld() != entity.getLocation().getWorld()) return;

            if(player.isDead()) return;
            if(player.getLocation().distance(entity.getLocation()) > 8){
                return;
            }

            final Action action;
            try{
                Field field = packet.getEntityUseActions().getField(0);
                field.setAccessible(true);
                Object obj = field.get(packet.getEntityUseActions().getTarget());
                String actionName = (obj == null) ? "" : obj.toString();

                switch(actionName){
                    case "INTERACT":
                        action = Action.RIGHT_CLICK_AIR;
                        break;
                    case "ATTACK":
                        action = Action.LEFT_CLICK_AIR;
                        break;
                    default:
                        return;
                }
            }catch(Exception e){
                e.printStackTrace();
                return;
            }

            packetEvent.setCancelled(true);
            Bukkit.getScheduler().runTask(SuperLibrary.plugin(), new Runnable() {
                @Override
                public void run(){
                    PlayerInteractNPCEvent event = new PlayerInteractNPCEvent(player, entity, action);
                    SuperLibrary.callEvent(event);
                }
            });
        }
    }

    @Override
    public ListeningWhitelist getSendingWhitelist(){
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist(){
        return ListeningWhitelist.newBuilder().
                priority(ListenerPriority.NORMAL).
                types(PacketType.Play.Client.USE_ENTITY).
                gamePhase(GamePhase.PLAYING).
                options(new ListenerOptions[0]).
                build();
    }

    @Override
    public Plugin getPlugin(){
        return SuperLibrary.plugin();
    }
}
