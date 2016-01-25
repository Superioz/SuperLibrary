package de.superioz.library.minecraft.server.common.npc.meta.entity;

import com.comphenix.protocol.events.PacketContainer;

/**
 * Class created on April in 2015
 * <p>
 * Just a helper class for all metadata values for setting with packets
 */
public enum EntityMeta {

    ENTITY_STATE(0),
    ENTITY_AIRLEVEL(1),
    LIVING_ENTITY_NAME(2),
    LIVING_ENTITY_SHOWNAME(3),
    LIVING_ENTITY_HEALTH(6),
    LIVING_ENTITY_POTION_EFFECT_COLOR(7),
    LIVING_ENTITY_POTION_EFFECT_AMBIENT(8),
    LIVING_ENTITY_ARROWS_INSIDE(9),
    LIVING_ENTITY_HAS_AI(15),
    AGEABLE_ENTITY_AGE(12),
    ARMORSTAND_STATE(10),
    ARMORSTAND_HEADPOS(11),
    ARMORSTAND_BODYPOS(12),
    ARMORSTAND_LEFT_ARMPOS(13),
    ARMORSTAND_RIGHT_ARMPOS(14),
    ARMORSTAND_LEFT_LEGPOS(15),
    ARMORSTAND_RIGHT_LEGPOS(16),
    HUMAN_SKIFLAGS(10),
    HUMAN_HIDECAPE(16),
    HUMAN_ABSORPTION_HEARTS(17),
    HUMAN_SCORE(18),
    HORSE_STATE(16),
    HORSE_TYPE(19),
    HORSE_COLOR(20),
    HORSE_OWNER(21),
    HORSE_ARMOR(22),
    BAT_STATE(16),
    TAMEABLE_ENTITY_STATE(16),
    TAMEABLE_ENTITY_OWNER(17),
    OCELOT_TYPE(18),
    WOLF_STATE(16),
    WOLF_HEALTH(18),
    WOLF_BEGGING(19),
    WOLF_COLLAR_COLOR(20),
    PIG_STATE(16),
    RABBIT_TYPE(18),
    SHEEP_COLOR_STATE(16),
    VILLAGER_PROFESSION(16),
    ENDERMAN_CARRY_BLOCK(16),
    ENDERMAN_CARRY_BLOCKDATA(17),
    ENDERMAN_ISSCREAMING(18),
    ZOMBIE_IS_CHILD(16),
    ZOMBIE_IS_VILLAGER(17),
    ZOMBIE_IS_CONVERTING(18),
    BLAZE_ON_FIRE(16),
    SPIDER_IS_CLIMBING(16),
    CREEPER_STATE(16),
    CREEPER_IS_POWERED(17),
    GHAST_IS_ATTACKING(16),
    SLIME_SIZE(16),
    SKELETON_TYPE(13),
    WITCH_IS_AGGRESSIVE(21),
    IRONGOLEM_BY_PLAYER(16),
    WITHER_WATCHED_TARGET(17),
    WITHER_INVULNERABLE_TIME(20);

    /**
     * Index is the number for packet metadata
     *
     * @see PacketContainer#getDataWatcherModifier()#index
     */
    private int index;

    /**
     * Constructor of metadata
     *
     * @param index The index {@link #index}
     */
    EntityMeta(int index){
        this.index = index;
    }

    /**
     * @return {@link #index}
     */
    public int getIndex(){
        return index;
    }
}
