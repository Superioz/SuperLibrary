package de.superioz.library.minecraft.server.common.npc;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeEntity;
import de.superioz.library.minecraft.server.listener.FakeMobListener;
import de.superioz.library.minecraft.server.listener.ProtocolListener;
import de.superioz.library.minecraft.server.util.BukkitUtilities;
import de.superioz.library.minecraft.server.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class NPCRegistry {

    private static List<CraftFakeEntity> entities = new ArrayList<>();

    /**
     * Registers an entity to the register
     *
     * @param fakeEntity The entity
     */
    public static void register(CraftFakeEntity fakeEntity){
        if(!entities.contains(fakeEntity))
            entities.add(fakeEntity);
    }

    /**
     * Unregisters given entity
     *
     * @param fakeEntity The entity
     */
    public static void unregister(CraftFakeEntity fakeEntity, boolean remove){
        if(entities.contains(fakeEntity)){
            List<UUID> uuidList = fakeEntity.getViewers();
            List<Player> playerList = new ArrayList<>();

            uuidList.forEach(uuid -> {
                Player pl = Bukkit.getPlayer(uuid);

                if(pl.isOnline()){
                    playerList.add(pl);
                }
            });

            fakeEntity.despawn(playerList.toArray(new Player[playerList.size()]));
            fakeEntity.setViewers(new ArrayList<>());

            if(remove)
                entities.remove(fakeEntity);
        }
    }

    /**
     * Unregisters all entities
     */
    public static void unregisterAll(){
        List<CraftFakeEntity> entityList = getEntities();
        entityList.forEach(fakeEntity -> NPCRegistry.unregister(fakeEntity, false));
        entities = new ArrayList<>();
    }

    /**
     * Gets all entities whose viewers contains given player
     *
     * @param player The player
     *
     * @return The list
     */
    public static List<CraftFakeEntity> getEntities(Player player){
        return getEntities().stream().filter(e -> e.getViewers().contains(player.getUniqueId())).collect(Collectors.toList());
    }

    /**
     * Updates all entities of given players
     *
     * @param players The players
     */
    public static void updatePlayerView(Player... players){
        for(Player p : players){
            for(CraftFakeEntity e : getEntities(p)){
                double distance = p.getLocation().distance(e.getLocation());

                if(!LocationUtil.checkRoughRange(distance, BukkitUtilities.CHUNK_WIDTH*4, BukkitUtilities.CHUNK_WIDTH))
                    return;
                e.update(p);
                System.out.println("Updated for " + p.getDisplayName() + "!");
            }
        }
    }

    /**
     * Gets the npc of given id
     *
     * @param id The id
     *
     * @return The entity
     */
    public static CraftFakeEntity getNPC(int id){
        for(CraftFakeEntity entity : getEntities()){
            if(entity.getUniqueId() == id)
                return entity;
        }
        return null;
    }

    /**
     * Initialises the NPCRegistry
     */
    public static void init(){
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(SuperLibrary.plugin(), new NPCAimUpdate(), 2L, 2L);

        // Register listener
        SuperLibrary.pluginManager().registerEvents(new FakeMobListener(), SuperLibrary.plugin());

        // ProtocolListener
        SuperLibrary.protocolManager().addPacketListener(new ProtocolListener());
    }

    // -- Intern methods

    public static List<CraftFakeEntity> getEntities(){
        return entities;
    }

}
