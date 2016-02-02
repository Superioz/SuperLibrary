package de.superioz.library.minecraft.server.common;

/**
 * Created on 02.02.2016.
 */
public enum ParticleEffect {

    EXPLODE("explode", 0),
    LARGE_EXPLODE("largeexplode", 1),
    HUGE_EXPLOSION("hugeexplosion", 2),
    FIREWORKS_SPARK("fireworksSpark", 3),
    BUBBLE("bubble", 4),
    SPLASH("splash", 5),
    WAKE("wake", 6),
    SUSPENDED("suspended", 7),
    DEPTH_SUSPENDED("depthsuspend", 8),
    CRIT("crit", 9),
    MAGIC_CRIT("magicCrit", 10),
    SMOKE("smoke", 11),
    LARGE_SMOKE("largesmoke", 12),
    SPELL("spell", 13),
    INSTANT_SPELL("instantSpell", 14),
    MOB_SPELL("mobSpell", 15),
    MOB_SPELL_AMBIENT("mobSpellAmbient", 16),
    WITCH_MAGIC("witchMagic", 17),
    DRIP_WATER("dripWater", 18),
    DRIP_LAVA("dripLava", 19),
    ANGRY_VILLAGER("angryVillager", 20),
    HAPPY_VILLAGER("happyVillager", 21),
    TOWN_AURA("townaura", 22),
    NOTE("note", 23),
    PORTAL("portal", 24),
    ENCHANTMENT_TABLE("enchantmenttable", 25),
    FLAME("flame", 26),
    LAVA("lava", 27),
    FOOTSTEP("footstep", 28),
    RED_DUST("reddust", 30),
    SNOWBALLPOOF("snowballpoof", 31),
    SLIME("slime", 33),
    HEART("heart", 34),
    BARRIER("barrier", 35),
    CLOUD("cloud", 29),
    SNOWSHOVEL("snowshovel", 32),
    DROPLET("droplet", 39),
    TAKE("take", 40),
    MOB_APPEARENCE("mobappearance", 41);

    public String name;
    public int id;

    ParticleEffect(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
