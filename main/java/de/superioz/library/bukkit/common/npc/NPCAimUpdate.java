package de.superioz.library.bukkit.common.npc;

import org.bukkit.entity.Player;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class NPCAimUpdate implements Runnable {

    @Override
    public void run(){
        for(NPC npc : NPCRegistry.getNpcRegistry()){
            if(npc.isFixedAim()) return;

            for(Player p : npc.getNearbyPlayers(5D)){
                npc.rotate(p.getEyeLocation().toVector());
            }
        }
    }
}
