package de.superioz.library.bukkit.listener;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.bukkit.common.npc.NPC;
import de.superioz.library.bukkit.common.npc.NPCRegistry;
import de.superioz.library.bukkit.common.protocol.PacketEvent;
import de.superioz.library.bukkit.common.protocol.PacketHandler;
import de.superioz.library.bukkit.common.protocol.PacketType;
import de.superioz.library.bukkit.common.protocol.WrappedPacket;
import de.superioz.library.bukkit.event.PlayerInteractNPCEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.lang.reflect.Field;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class ProtocolListener implements PacketHandler {


    @Override
    public void onReceive(PacketEvent event){
        WrappedPacket packet = event.getPacket();
        final Player player = event.getPlayer();

        if(event.getType() == PacketType.Play.Client.USE_ENTITY){
            int id = (int) event.getPacket().getIntegers().read(0);

            if(id < 0) return;
            final NPC entity = NPCRegistry.getNPC(id);
            if(entity == null || player.getWorld() != entity.getLocation().getWorld()) return;

            if(player.isDead()) return;
            if(player.getLocation().distance(entity.getLocation()) > 8){
                return;
            }

            final Action action;
            try{
                Field field = packet.getEntityUseActions().getField(0);
                field.setAccessible(true);
                Object obj = field.get(packet.getEntityUseActions().getClassInstance());
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

            event.setCancelled(true);
            Bukkit.getScheduler().runTask(BukkitLibrary.plugin(), new Runnable() {
                @Override
                public void run(){
                    PlayerInteractNPCEvent event = new PlayerInteractNPCEvent(player, entity, action);
                    BukkitLibrary.callEvent(event);
                }
            });
        }
    }

    @Override
    public void onSend(PacketEvent event){

    }
}
