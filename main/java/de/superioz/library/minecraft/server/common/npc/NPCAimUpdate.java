package de.superioz.library.minecraft.server.common.npc;

import de.superioz.library.minecraft.server.common.npc.NPCRegistry;
import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeEntity;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class NPCAimUpdate implements Runnable {

    @Override
    public void run(){
        for(CraftFakeEntity entity : NPCRegistry.getEntities()){
            if(entity.getSettings().isFixedAim()) return;

            for(Player p : entity.getNearbyPlayers(5D)){
                entity.rotate(p.getEyeLocation().toVector(), p);
            }
        }
    }
}
