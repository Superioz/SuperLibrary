package de.superioz.library.minecraft.server.lab.fakemob.data.entity;

import org.bukkit.entity.Ocelot;

/**
 * Class created on April in 2015
 *
 * Values from metadata and a bit too much work to describe every one.
 * The names should explain all important things
 * @see EntityMetadata
 */
public class EntityMetadataValues {

    public enum EntityStateMeta {
        NORMAL(0x00),
        ON_FIRE(0x1),
        CROUCHED(0x2),
        SPRINTING(0x8),
        EATING(0x10),
        INVISIBLE(0x20);

        public int value;
        EntityStateMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum ArmorStandStateMeta {
        SMALL(0x1),
        HAS_GRAVITY(0x02),
        HAS_ARMS(0x4),
        HAS_BASEPLATE(0x8);

        public int value;
        ArmorStandStateMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum TameableStateMeta {
        SITTING(0x1),
        TAMED(0x4);

        public int value;
        TameableStateMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum OcelotTypeMeta {
        WILD(Ocelot.Type.WILD_OCELOT.ordinal()),
        BLACK_CAT(Ocelot.Type.BLACK_CAT.ordinal()),
        RED_CAT(Ocelot.Type.RED_CAT.ordinal()),
        STAMESE_CAT(Ocelot.Type.SIAMESE_CAT.ordinal());

        public int value;
        OcelotTypeMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum WolfStateMeta {
        ANGRY(0x2);

        public Object value;
        WolfStateMeta(Object value){
            this.value = value;
        }
        public Object v(){
            return value;
        }
    }

    public enum RabbitTypeMeta {
        BROWN(0),
        WHITE(1),
        BLACK(2),
        BLACK_AND_WHITE(3),
        GOLD(4),
        SALT_AND_PEPPER(5),
        KILLER(99);

        public int value;
        RabbitTypeMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum SheepStateMeta {
        SHEARED(0x10);

        public int value;
        SheepStateMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum VillagerProfessionMeta {
        FARMER(0),
        LIBRARIAN(1),
        PRIEST(2),
        BLACKSMITH(3),
        BUTCHER(4);

        public int value;
        VillagerProfessionMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum CreeperStateMeta {
        IDLE(-1),
        FUSE(1);

        public int value;
        CreeperStateMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public enum SkeletonTypeMeta {
        NORMAL(0),
        WITHER(1);

        public int value;
        SkeletonTypeMeta(int value){
            this.value = value;
        }
        public int v(){
            return value;
        }
    }

    public static class HorseMeta {

        public enum HorseStateMeta {
            TAMED(0x2),
            HAS_SADDLE(0x4),
            HAS_CHEST(0x8),
            BRED(0x10),
            EATING(0x20),
            REARING(0x40),
            HAS_MOUTH_OPEN(0x80);

            public int value;
            HorseStateMeta(int value){
                this.value = value;
            }
            public int v(){
                return value;
            }
        }

        public enum HorseTypeMeta {
            HORSE(0),
            DONKEY(1),
            MULE(2),
            ZOMBIE(3),
            SKELETON(4);

            public int value;
            HorseTypeMeta(int value){
                this.value = value;
            }
            public int v(){
                return value;
            }
        }

        public enum HorseColorMeta {
            WHITE(0),
            CREAMY(1),
            CHESTNUT(3),
            BROWN(4),
            BLACK(5),
            GRAY(6),
            DARK_DOWN(7);

            public int value;
            HorseColorMeta(int value){
                this.value = value;
            }
            public int v(){
                return value;
            }
        }

        public enum HorseStyleMeta {
            NONE(0),
            WHITE(1),
            WHITEFIELD(2),
            WHITE_DOTS(3),
            BLACK_DOTS(4);

            public int value;
            HorseStyleMeta(int value){
                this.value = value;
            }
            public int v(){
                return value;
            }
        }

        public enum HorseArmorMeta {
            NONE(0),
            IRON(1),
            GOLD(2),
            DIAMOND(3);

            public int value;
            HorseArmorMeta(int value){
                this.value = value;
            }
            public int v(){
                return value;
            }
        }
    }

}
