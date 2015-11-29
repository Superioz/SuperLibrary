package de.superioz.library.minecraft.server.common.lab.disguise;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import de.superioz.library.java.util.SimpleStringUtils;
import de.superioz.library.minecraft.server.util.BukkitUtil;

/**
 * Enum for disguisable entities
 */
public enum DisguiseType {

    ZOMBIE("EntityZombie"),
    WITHER_SKELETON("EntitySkeleton"),
    SKELETON("EntitySkeleton"),
    ZOMBIE_PIGMAN("EntityPigZombie"),
    BLAZE("EntityBlaze"),
    ENDERMAN("EntityEnderman"),
    CREEPER("EntityCreeper"),
    SPIDER("EntitySpider"),
    WITCH("EntityWitch"),
    WITHER("EntityWither"),
    GHAST("EntityGhast"),
    GIANT("EntityGiantZombie"),
    SLIME("EntitySlime"),
    CAVE_SPIDER("EntityCaveSpider"),
    SILVERFISH("EntitySilverfish"),
    MAGMA_CUBE("EntityMagmaCube"),
    BAT("EntityBat"),
    PIG("EntityPig"),
    SHEEP("EntitySheep"),
    COW("EntityCow"),
    CHICKEN("EntityChicken"),
    SQUID("EntitySquid"),
    WOLF("EntityWolf"),
    OCELOT("EntityOcelot"),
    HORSE("EntityHorse"),
    VILLAGER("EntityVillager"),
    IRON_GOLEM("EntityIronGolem"),
    SNOWMAN("EntitySnowman"),
    ENDER_DRAGON("EntityEnderDragon"),
    ENDERMITE("EntityEndermite"),
    RABBIT("EntityRabbit"),
    GUARDIAN("EntityGuardian"),
    MUSHROOM("EntityMushroomCow"),
    MINECART("EntityArrow", 10),
    BOAT("EntityArrow", 1),
    ARMORSTAND("EntityArrow", 78);

    private final String cls;
    private int id;
    private WrappedBlockData b;

    DisguiseType(String cls, int... id) {
        this.cls = cls;

        if(id.length >= 1)
            this.id = id[0];
    }

    DisguiseType(String cls, WrappedBlockData data, int... id){
        this.cls = cls;
        b = data;

        if(id.length >= 1)
            this.id = id[0];
    }

    public int getId(){
        return id;
    }

    public WrappedBlockData getBlockData(){
        return b;
    }

    public String getTypeName(){
        String name = name();
        return SimpleStringUtils.upperFirstLetterSpaced(name, "_");
    }

    public String getClassName() {
        return BukkitUtil.getNMSPackage() + "." + cls;
    }
}
