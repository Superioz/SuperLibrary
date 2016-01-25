package de.superioz.library.minecraft.server.common.npc;

import de.superioz.library.minecraft.server.common.npc.meta.entity.EntityMeta;
import de.superioz.library.minecraft.server.common.npc.meta.entity.EntityMetaValues;
import de.superioz.library.minecraft.server.common.npc.meta.entity.MobType;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntityAppearence;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntitySettings;
import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeMob;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
@Getter
public class FakeMob extends CraftFakeMob {

    public MobType type;

    protected FakeMob(EntityAppearence appearence, EntitySettings settings){
        super(appearence, settings);
    }

    public FakeMob(MobType type, Location loc, String name, EntitySettings settings){
        super(new EntityAppearence(name, loc, type.getType()), settings);
        this.type = type;
    }

    /**
     * Set the mob hanging (only if it's a bat)
     *
     * @param flag The flag
     */
    public void setHanging(boolean flag){
        if(getType() != MobType.BAT) return;
        super.setMetaData(EntityMeta.BAT_STATE, flag);
    }

    /**
     * Set the age of this entity (negative for child)
     *
     * @param age The age as int
     */
    public void setAge(int age){
        super.setMetaData(EntityMeta.AGEABLE_ENTITY_AGE, (byte)age);
    }

    /**
     * Set the horse type
     *
     * @param type The type
     */
    public void setHorseType(EntityMetaValues.HorseMeta.HorseTypeMeta type){
        if(getType() != MobType.HORSE) return;
        super.setMetaData(EntityMeta.HORSE_TYPE, type.v());
    }

    /**
     * Set the horse color
     *
     * @param color The color
     */
    public void setHorseColor(EntityMetaValues.HorseMeta.HorseColorMeta color){
        if(getType() != MobType.HORSE) return;
        super.setMetaData(EntityMeta.HORSE_COLOR, color.v());
    }

    /**
     * Set horse owner
     *
     * @param name The name of the owner
     */
    public void setHorseOwner(String name){
        if(getType() != MobType.HORSE) return;
        super.setMetaData(EntityMeta.HORSE_OWNER, name);
    }

    /**
     * Set horse armor
     *
     * @param armor The armor
     */
    public void setHorseArmor(EntityMetaValues.HorseMeta.HorseArmorMeta armor){
        if(getType() != MobType.HORSE) return;
        super.setMetaData(EntityMeta.HORSE_ARMOR, armor.v());
    }

    /**
     * Set the owner of the tameable object
     *
     * @param name The name of the owner
     */
    public void setOwner(String name){
        super.setMetaData(EntityMeta.TAMEABLE_ENTITY_OWNER, name);
    }

    /**
     * Set the type of the ocelot
     *
     * @param ocelotType The ocelot type
     */
    public void setOcelotType(EntityMetaValues.OcelotTypeMeta ocelotType){
        if(getType() != MobType.OCELOT) return;
        super.setMetaData(EntityMeta.OCELOT_TYPE, ocelotType.v());
    }

    /**
     * Set villager profession
     *
     * @param profession The profession
     */
    public void setVillagerProfession(EntityMetaValues.VillagerProfessionMeta profession){
        if(getType() != MobType.VILLAGER) return;
        super.setMetaData(EntityMeta.VILLAGER_PROFESSION, profession.v());
    }

    /**
     * Set the block holding in enderman's hand
     *
     * @param material The material
     */
    public void setEndermanCarriedBlock(Material material){
        if(getType() != MobType.ENDERMAN) return;
        super.setMetaData(EntityMeta.ENDERMAN_CARRY_BLOCK, material.getId());
    }

    /**
     * Set the data for the block holding in enderman's hand
     *
     * @param data The data
     */
    public void setEndermanCarriedBlockData(byte data){
        if(getType() != MobType.ENDERMAN) return;
        super.setMetaData(EntityMeta.ENDERMAN_CARRY_BLOCKDATA, data);
    }

    /**
     * Set if the enderman should scream
     *
     * @param flag The flag
     */
    public void setScreaming(boolean flag){
        if(getType() != MobType.ENDERMAN) return;
        super.setMetaData(EntityMeta.ENDERMAN_ISSCREAMING, flag);
    }

    /**
     * Set if the zombie gets a child
     *
     * @param flag The flag
     */
    public void setZombieChild(boolean flag){
        if(getType() != MobType.ZOMBIE
                || getType() != MobType.PIG_ZOMBIE) return;
        super.setMetaData(EntityMeta.ZOMBIE_IS_CHILD, flag);
    }

    /**
     * Set if the zombie gets a zombie villager
     *
     * @param flag The flag
     */
    public void setZombieVillager(boolean flag){
        if(getType() != MobType.ZOMBIE) return;
        super.setMetaData(EntityMeta.ZOMBIE_IS_VILLAGER, flag);
    }

    /**
     * Set if the zombie gets converting (from villager or to villager)
     *
     * @param flag The flag
     */
    public void setZombieConverting(boolean flag){
        if(getType() != MobType.ZOMBIE) return;
        super.setMetaData(EntityMeta.ZOMBIE_IS_CONVERTING, flag);
    }

    /**
     * Set the blaze on fire or not
     *
     * @param flag The flag
     */
    public void setBlazeOnFire(boolean flag){
        if(getType() != MobType.BLAZE) return;
        super.setMetaData(EntityMeta.BLAZE_ON_FIRE, flag);
    }

    /**
     * Set spider climbing
     *
     * @param flag The flag
     */
    public void setSpiderClimbing(boolean flag){
        if(getType() != MobType.SPIDER
                || getType() != MobType.CAVE_SPIDER) return;
        super.setMetaData(EntityMeta.SPIDER_IS_CLIMBING, flag);
    }

    /**
     * Set the creeper state
     *
     * @param state The state
     */
    public void setCreeperState(EntityMetaValues.CreeperStateMeta state){
        if(getType() != MobType.CREEPER) return;
        super.setMetaData(EntityMeta.CREEPER_STATE, state.v());
    }

    /**
     * Set the creeper powered or not
     *
     * @param flag The flag
     */
    public void setCreeperPowered(boolean flag){
        if(getType() != MobType.CREEPER) return;
        super.setMetaData(EntityMeta.CREEPER_IS_POWERED, flag);
    }

    /**
     * Set the ghast attacking or not
     *
     * @param flag The flag
     */
    public void setGhastAttacking(boolean flag){
        if(getType() != MobType.GHAST) return;
        super.setMetaData(EntityMeta.GHAST_IS_ATTACKING, flag);
    }

    /**
     * Set the size of the (magma-) slime
     *
     * @param size The size as an integer
     */
    public void setSlimeSize(int size){
        if(getType() != MobType.SLIME
                || getType() != MobType.MAGMA_CUBE) return;
        super.setMetaData(EntityMeta.BLAZE_ON_FIRE, size);
    }

    /**
     * Set if the witch should be agressive
     *
     * @param flag The flag
     */
    public void setWitchAgressive(boolean flag){
        if(getType() != MobType.WITCH) return;
        super.setMetaData(EntityMeta.WITCH_IS_AGGRESSIVE, flag);
    }

}
