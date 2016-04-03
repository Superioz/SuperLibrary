package de.superioz.library.bukkit.common.npc;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.bukkit.common.protocol.ProtocolEntityMeta;
import de.superioz.library.bukkit.common.protocol.WrappedDataWatcher;
import de.superioz.library.bukkit.listener.FakeMobListener;
import de.superioz.library.bukkit.listener.ProtocolListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

/**
 * Created on 01.04.2016.
 */
public class NPCHandler {

	/**
	 * Initialises the npc handler
	 */
	public static void init(){
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(BukkitLibrary.plugin(), new NPCAimUpdate(), 2L, 2L);

		// Register listener
		BukkitLibrary.pluginManager().registerEvents(new FakeMobListener(), BukkitLibrary.plugin());

		// ProtocolListener
		BukkitLibrary.addPacketHandler(new ProtocolListener());
	}

	/**
	 * Gets a datawatcher with default values for given entity type
	 *
	 * @param type The type
	 */
	public static void setDefaultWatchableObjects(WrappedDataWatcher dataWatcher, EntityType type){
		dataWatcher.set(ProtocolEntityMeta.ENTITY_CUSTOM_NAME_VISIBLE, true);
		dataWatcher.set(ProtocolEntityMeta.ENTITY_IS_SILENT, true);
		dataWatcher.set(ProtocolEntityMeta.LIVING_HEALTH, 1.0F);
		dataWatcher.set(ProtocolEntityMeta.LIVING_POTION_EFFECT_COLOR, 0);
		dataWatcher.set(ProtocolEntityMeta.LIVING_IS_POTION_EFFECT_AMBIENT, false);
		dataWatcher.set(ProtocolEntityMeta.LIVING_NUMBER_OF_ARROWS_INSIDE, (byte) 0);

		// Get datawatcherobjects from entitytype
		switch(type){
			case BAT:
				dataWatcher.set(ProtocolEntityMeta.BAT_IS_HANGING, (byte) 0);
				break;
			case BLAZE:
				dataWatcher.set(ProtocolEntityMeta.BLAZE_IS_ON_FIRE, (byte) 0);
				break;
			case SPIDER:
			case CAVE_SPIDER:
				dataWatcher.set(ProtocolEntityMeta.SPIDER_IS_CLIMBING, (byte) 0);
				break;
			case CREEPER:
				dataWatcher.set(ProtocolEntityMeta.CREEPER_STATE, (byte) -1);
				dataWatcher.set(ProtocolEntityMeta.CREEPER_IS_CHARGED, false);
				dataWatcher.set(ProtocolEntityMeta.CREEPER_IS_IGNITED, false);
				break;
			case ENDERMAN:
				dataWatcher.set(ProtocolEntityMeta.ENDERMAN_CARRIED_BLOCK, (byte) 0);
				dataWatcher.set(ProtocolEntityMeta.ENDERMAN_IS_SCREAMING, false);
				break;
			case ENDER_DRAGON:
				break;
			case GHAST:
				dataWatcher.set(ProtocolEntityMeta.GHAST_IS_ATTACKING, false);
				break;
			case GIANT:
				break;
			case HORSE:
				dataWatcher.set(ProtocolEntityMeta.HORSE_STATE, (byte)0);
				dataWatcher.set(ProtocolEntityMeta.HORSE_COLOR_STYLE, 0);
				dataWatcher.set(ProtocolEntityMeta.HORSE_VARIANT, 0);
				dataWatcher.set(ProtocolEntityMeta.HORSE_OWNER, "");
				dataWatcher.set(ProtocolEntityMeta.HORSE_ARMOR, 0);
				break;
			case IRON_GOLEM:
				dataWatcher.set(ProtocolEntityMeta.IRONGOLEM_IS_PLAYER_MADE, (byte) 0);
				break;
			case SLIME:
			case MAGMA_CUBE:
				dataWatcher.set(ProtocolEntityMeta.SLIME_SIZE, 1);
				break;
			case OCELOT:
				dataWatcher.set(ProtocolEntityMeta.OCELOT_TYPE, 0);
				break;
			case PIG:
				dataWatcher.set(ProtocolEntityMeta.PIG_HAS_SADDLE, false);
				break;
			case PIG_ZOMBIE:
			case ZOMBIE:
				dataWatcher.set(ProtocolEntityMeta.ZOMBIE_IS_BABY, false);
				dataWatcher.set(ProtocolEntityMeta.ZOMBIE_IS_CONVERTING, false);
				dataWatcher.set(ProtocolEntityMeta.ZOMBIE_IS_VILLAGER, 0);
				dataWatcher.set(ProtocolEntityMeta.ZOMBIE_ARE_HANDS_HELD_UP, false);
				break;
			case RABBIT:
				dataWatcher.set(ProtocolEntityMeta.RABBIT_TYPE, 0);
				break;
			case SHEEP:
				dataWatcher.set(ProtocolEntityMeta.SHEEP_STATUS, (byte) 0);
				break;
			case SKELETON:
				dataWatcher.set(ProtocolEntityMeta.SKELETON_TYPE, 0);
				break;
			case VILLAGER:
				dataWatcher.set(ProtocolEntityMeta.VILLAGER_PROFESSION, 0);
				break;
			case WITCH:
				dataWatcher.set(ProtocolEntityMeta.WITCH_IS_AGRESSIVE, false);
				break;
			case WITHER:
				dataWatcher.set(ProtocolEntityMeta.WITHER_INVULNERABLE_TIME, 0);
				dataWatcher.set(ProtocolEntityMeta.WITHER_TARGET_FIRST, 0);
				break;
			case WOLF:
				dataWatcher.set(ProtocolEntityMeta.WOLF_DAMAGE_TAKEN, 0F);
				dataWatcher.set(ProtocolEntityMeta.WOLF_IS_BEGGING, false);
				dataWatcher.set(ProtocolEntityMeta.WOLF_COLLAR_COLOR, 14);
				break;
			default:
				break;
		}
	}

}
