package de.superioz.library.minecraft.server.common.npc.meta;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeEntity;
import de.superioz.library.minecraft.server.common.npc.raw.holo.CraftHologramPart;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.protocol.ProtocolUtil;
import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import org.bukkit.entity.EntityType;

/**
 * Class created on April in 2015
 * <p>
 * Get datawatcher for defaults from entity
 */
public class EntityMetaHandler {

    /**
     * Returns a default datawatcher for given entitytype
     *
     * @see WrappedDataWatcher
     */
    public static WrappedDataWatcher getEntityDefaults(CraftFakeEntity npc){
        WrappedDataWatcher metaData = fromEntity(npc.getEntityType());

        // Default metadata not type specific
        if(npc.getName() != null && !npc.getName().isEmpty()
                && npc.nameShown()){
            metaData.setObject(EntityMeta.LIVING_ENTITY_NAME.getIndex(),
                    ChatUtil.colored(npc.getName()));
            metaData.setObject(EntityMeta.LIVING_ENTITY_SHOWNAME.getIndex(),
                    ProtocolUtil.toByte(npc.nameShown()));
        }

        return metaData;
    }

    /**
     * Get default DataWatcher for a hologram
     *
     * @return The {@link WrappedDataWatcher} for it
     *
     * @see WrappedDataWatcher
     */
    public static WrappedDataWatcher getHologramDefaults(CraftHologramPart part){
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        dataWatcher.setObject(EntityMeta.ARMORSTAND_STATE.getIndex(), (byte) EntityMetaValues
                .ArmorStandStateMeta.SMALL.v());
        dataWatcher.setObject(EntityMeta.LIVING_ENTITY_HEALTH.getIndex(), 1.0F);
        dataWatcher.setObject(EntityMeta.LIVING_ENTITY_POTION_EFFECT_COLOR.getIndex(), 0);
        dataWatcher.setObject(EntityMeta.LIVING_ENTITY_POTION_EFFECT_AMBIENT.getIndex(), (byte) 0);
        dataWatcher.setObject(EntityMeta.LIVING_ENTITY_ARROWS_INSIDE.getIndex(), (byte) 0);
        dataWatcher.setObject(EntityMeta.ENTITY_STATE.getIndex()
                , ProtocolUtil.toByte(EntityMetaValues.EntityStateMeta.INVISIBLE.v()));
        dataWatcher.setObject(EntityMeta.LIVING_ENTITY_SHOWNAME.getIndex(), ProtocolUtil.toByte(true));

        if(part.getText() != null && !part.getText().isEmpty()){
            dataWatcher.setObject(EntityMeta.LIVING_ENTITY_NAME.getIndex(),
                    ChatUtil.colored(part.getText()));
            dataWatcher.setObject(EntityMeta.LIVING_ENTITY_SHOWNAME.getIndex(),
                    ProtocolUtil.toByte(1));
        }

        return dataWatcher;
    }

    /**
     * Sets in the datawatcher the value in the specific index
     *
     * @param dataWatcher The wrapped data watcher object
     * @param metadata    The metadata index
     * @param value       The new value for it
     *
     * @return The datawatcher object with index changed/set
     *
     * @see WrappedDataWatcher
     * @see EntityMeta
     */
    public static WrappedDataWatcher setMetadataValue(WrappedDataWatcher dataWatcher
            , EntityMeta metadata, Object value){
        dataWatcher.setObject(metadata.getIndex(), value);
        return dataWatcher;
    }

    /**
     * Gets a default datawatcher for given entity
     *
     * @param type The {@link EntityType}
     *
     * @return The default {@link WrappedDataWatcher} from given type
     *
     * @see WrappedDataWatcher
     * @see EntityType
     */
    private static WrappedDataWatcher fromEntity(EntityType type){
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject(EntityMeta.LIVING_ENTITY_HEALTH.getIndex(), 1.0F); // Health
        watcher.setObject(EntityMeta.LIVING_ENTITY_POTION_EFFECT_COLOR.getIndex(), 0); // Potion Effect color
        watcher.setObject(EntityMeta.LIVING_ENTITY_POTION_EFFECT_AMBIENT.getIndex(), (byte) 0); // Potion effect ambient
        watcher.setObject(EntityMeta.LIVING_ENTITY_ARROWS_INSIDE.getIndex(), (byte) 0); // Number of arrows in entity

        switch(type){
            case BAT:
                watcher.setObject(16, (byte) 0); // Hanging
                break;
            case BLAZE:
                watcher.setObject(16, (byte) 0); // On fire
                break;
            case SPIDER:
            case CAVE_SPIDER:
                watcher.setObject(16, (byte) 0); // Climbing
                break;
            case CREEPER:
                watcher.setObject(16, (byte) -1); // State -1 = idle; 1 = fuse
                watcher.setObject(17, (byte) 0); // Powered
                break;
            case ENDERMAN:
                watcher.setObject(16, (short) 0); //Carried Block
                watcher.setObject(17, (byte) 0); //Carried Block Data
                watcher.setObject(18, (byte) 0); //Is screaming?
                break;
            case ENDER_DRAGON:
                break;
            case GHAST:
                watcher.setObject(16, (byte) 0); // Is attacking
                break;
            case GIANT:
                break;
            case HORSE:
                watcher.setObject(16, 0); // -
                watcher.setObject(19, (byte) 0); // Type
                watcher.setObject(20, 0); // Style
                watcher.setObject(21, ""); // Owner Name
                watcher.setObject(22, 0); // Armor
                break;
            case IRON_GOLEM:
                watcher.setObject(16, (byte) 0); // Made by player?
                break;
            case SLIME:
            case MAGMA_CUBE:
                watcher.setObject(16, (byte) 1); // Slime size 1
                break;
            case OCELOT:
                watcher.setObject(18, (byte) 0); // Ocelot Type
                break;
            case PIG:
                watcher.setObject(16, (byte) 0); // Has saddle
                break;
            case PIG_ZOMBIE:
            case ZOMBIE:
                watcher.setObject(12, (byte) 0); // Is child
                watcher.setObject(13, (byte) 0); // Is villager
                watcher.setObject(14, (byte) 0); // Is converting
                break;
            case RABBIT:
                watcher.setObject(18, (byte) 0); // Rabbit type
                break;
            case SHEEP:
                watcher.setObject(16, (byte) 0); // Color
                break;
            case SKELETON:
                watcher.setObject(13, (byte) 0); // Type. 0 = Normal, 1 = Wither skeleton
                break;
            case VILLAGER:
                watcher.setObject(16, 0); //Type
                break;
            case WITCH:
                watcher.setObject(21, (byte) 0); // Is agressive
                break;
            case WITHER:
                watcher.setObject(17, 0); // Watched Target
                watcher.setObject(18, 0); // ""
                watcher.setObject(19, 0); // ""
                watcher.setObject(20, 0); // Invulnerable time
                break;
            case WOLF:
                watcher.setObject(18, 20.0f); //Health
                watcher.setObject(19, (byte) 0); //Begging
                watcher.setObject(20, (byte) 14); //Collar color
                break;
            default:
                break;
        }

        return watcher;
    }

}
