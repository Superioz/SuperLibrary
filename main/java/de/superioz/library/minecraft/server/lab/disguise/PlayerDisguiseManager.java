package de.superioz.library.minecraft.server.lab.disguise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.util.BukkitUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class PlayerDisguiseManager implements Listener {

    public static HashMap<UUID, PlayerDisguise> disguised = new HashMap<>();

    public void addPacketListener(){
        SuperLibrary.protocolManager().addPacketListener(new PacketAdapter(SuperLibrary.plugin(),
                ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_ENTITY_SPAWN){
            @Override
            public void onPacketReceiving(PacketEvent event) {
            }

            @Override
            public void onPacketSending(PacketEvent event){
                PacketContainer packet = event.getPacket();
                Player toDisplay = (Player) packet.getEntityModifier(event).readSafely(0);
                Player receiver = event.getPlayer();

                if(disguised.containsKey(toDisplay.getUniqueId())){
                    event.setCancelled(true);
                    PlayerDisguise disguise = disguised.get(toDisplay.getUniqueId());

                    new BukkitRunnable() {
                        @Override
                        public void run(){
                            disguise.send(receiver);
                        }
                    }.runTaskLater(SuperLibrary.plugin(), 1L);
                }
            }
        });

        SuperLibrary.protocolManager().addPacketListener(new PacketAdapter(SuperLibrary.plugin(),
                ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event){
                PacketContainer packet = event.getPacket();
                int id = packet.getIntegers().read(0);
                Player clicker = event.getPlayer();
                Entity clicked = BukkitUtil.getEntity(clicker.getWorld(), id);

                if(clicked == null){
                    return;
                }

                if(!(clicked instanceof Player)){
                    return;
                }

                Player clickedPlayer = (Player) clicked;

                if(disguised.containsKey(clickedPlayer.getUniqueId())){
                    event.setCancelled(true);
                }
            }

            @Override
            public void onPacketSending(PacketEvent event){}
        });
    }

    @EventHandler
    public void onPlayerDisguise(PlayerDisguiseEvent event) {
        disguised.put(event.getWho().getUniqueId(), event.getDisguise());
    }

    @EventHandler
    public void onPlayerUndisguise(PlayerUndisguiseEvent event) {
        if(disguised.containsKey(event.getWho().getUniqueId()))
            disguised.remove(event.getWho().getUniqueId());
    }

}
