package de.superioz.library.minecraft.server.common.npc.meta.entity;

import de.superioz.library.java.util.RandomUtil;
import org.bukkit.entity.EntityType;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public enum MobType {

    BAT(EntityType.BAT),
    BLAZE(EntityType.BLAZE),
    SPIDER(EntityType.SPIDER),
    CAVE_SPIDER(EntityType.CAVE_SPIDER),
    CREEPER(EntityType.CREEPER),
    ENDERMAN(EntityType.ENDERMAN),
    ENDER_DRAGON(EntityType.ENDER_DRAGON),
    GHAST(EntityType.GHAST),
    GIANT(EntityType.GIANT),
    HORSE(EntityType.HORSE),
    IRON_GOLEM(EntityType.IRON_GOLEM),
    SLIME(EntityType.SLIME),
    MAGMA_CUBE(EntityType.MAGMA_CUBE),
    OCELOT(EntityType.OCELOT),
    PIG(EntityType.PIG),
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE),
    ZOMBIE(EntityType.ZOMBIE),
    RABBIT(EntityType.RABBIT),
    SHEEP(EntityType.SHEEP),
    SKELETON(EntityType.SKELETON),
    VILLAGER(EntityType.VILLAGER),
    WITCH(EntityType.WITCH),
    WITHER(EntityType.WITHER),
    WOLF(EntityType.WOLF);

    EntityType type;

    MobType(EntityType type){
        this.type = type;
    }

    /**
     * Gets the type
     *
     * @return The type
     */
    public EntityType getType(){
        return type;
    }

    /**
     * Gets a random mobtype
     *
     * @return The mob type
     */
    public static MobType random(){
        int rInt = RandomUtil.getInteger(0, values().length-1);
        return values()[rInt];
    }

}
