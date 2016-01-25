package de.superioz.library.minecraft.server.common.npc;

import de.superioz.library.minecraft.server.common.npc.raw.holo.CraftHologram;
import org.bukkit.Location;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class Hologram extends CraftHologram {

    public Hologram(Location loc, String... lines){
        super(loc, lines);
    }

    public Hologram(Location loc){
        super(loc, "&aFirst line!", "&cSecond line ;)", "&cThird line <3", "&9Fourth line :3", "&5Fifth line :)");
    }

}
