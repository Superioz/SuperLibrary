package de.superioz.library.bukkit.common.protocol;

import lombok.Getter;

/**
 * Created on 29.03.2016.
 */
@Getter
public enum ProtocolEntityType {

	// Monsters
	CREEPER(50, true, 0.6, 1.8),
	SKELETON(51, true, 0.6, 1.95),
	SPIDER(52, true, 1.4, 0.9),
	GIANT_ZOMBIE(53, true, 0.6 * 6, 1.8 * 6),
	ZOMBIE(54, true, 0.6, 1.8),
	SLIME(55, true, 0.51000005, 0.51000005),
	GHAST(56, true, 4, 4),
	ZOMBIE_PIGMAN(57, true, 0.6, 1.8),
	ENDERMAN(58, true, 0.6, 2.9),
	CAVE_SPIDER(59, true, 0.7, 0.5),
	SILVERFISH(60, true, 0.4, 0.3),
	BLAZE(61, true, 0.6, 1.8),
	MAGMA_CUBE(62, true, 0.51000005, 0.51000005),
	ENDER_DRAGON(63, true, 16, 8),
	WITHER(64, true, 0.9, 3.5),
	BAT(65, true, 0.5, 0.9),
	WITCH(66, true, 0.6, 1.8),
	ENDERMITE(67, true, 0.4, 0.3),
	GUARDIAN(68, true, 0.85, 0.85),
	SHULKER(69, true, 1, 1),

	// Mobs
	PIG(90, true, 0.9, 0.9),
	SHEEP(91, true, 0.9, 1.3),
	COW(92, true, 0.9, 1.3),
	CHICKEN(93, true, 0.4, 0.7),
	SQUID(94, true, 0.95, 0.95),
	WOLF(95, true, 0.6, 0.8),
	MUSHROOM_COW(96, true, 0.9, 1.3),
	SNOWMAN(97, true, 0.7, 1.9),
	OCELOT(98, true, 0.6, 0.8),
	IRON_GOLEM(99, true, 1.4, 2.9),
	HORSE(100, true, 1.4, 1.6),
	RABBIT(101, true, 0.6, 0.7),
	VILLAGER(120, true, 0.6, 1.8),

	// Entities
	BOAT(1, false, 1.5, 0.6),
	ITEM_STACK_SLOT(2, false, 0.25, 0.25),
	AREA_EFFECT_CLOUD(3, false, -1, -1),
	MINECART(10, false, 0.98, 0.7),
	ACTIVATED_TNT(50, false, 0.98, 0.98),
	ENDER_CRYSTAL(51, false, 2, 2),
	PROJECTILE_ARROW(60, false, 0.5, 0.5),
	PROJECTILE_SNOWBALL(61, false, 0.25, 0.25),
	PROJECTILE_EGG(62, false, 0.25, 0.25),
	PROJECTILE_GHAST_FIREBALL(63, false, 1, 1),
	PROJECTILE_BLAZE_FIRECHARGE(64, false, 0.3125, 0.3125),
	THROWN_ENDERPEARL(65, false, 0.25, 0.25),
	PROJECTILE_WITHERSKULL(66, false, 0.3125, 0.3125),
	SHULKER_BULLET(67, false, 0.3125, 0.3125),
	FALLING_OBJECTS(70, false, 0.98, 0.98),
	ITEM_FRAMES(71, false, -1, -1),
	EYE_OF_ENDER(72, false, 0.25, 0.25),
	THROWN_POTION(73, false, 0.25, 0.25),
	FALLING_DRAGON_EGG(74, false, 0.98, 0.98),
	THROWN_EXP_BOTTLE(75, false, 0.25, 0.25),
	FIREWORK_ROCKET(76, false, 0.25, 0.25),
	LEASH_KNOT(77, false, 0.5, 0.5),
	ARMOR_STAND(78, false, 0.5, 2),
	FISHING_FLOAT(90, false, 0.25, 0.25),
	SPECTRAL_ARROW(91, false, 0.5, 0.5),
	TIPPED_ARROW(92, false, 0.5, 0.5),
	DRAGON_FIREBALL(93, false, 0.3125, 0.3125);


	private int identifier;
	private boolean living;
	private double width;
	private double height;

	/**
	 * Constructor for the protocol protocol type
	 *
	 * @param living Living protocol (mob/monster) or normal (item frame)
	 * @param height The height of the protocol
	 * @param width  The width of the protocol
	 */
	ProtocolEntityType(int identifier, boolean living, double width, double height){
		this.identifier = identifier;
		this.living = living;
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets entity type from given id
	 *
	 * @param identifier The id
	 * @return The entity type
	 */
	public static ProtocolEntityType fromEID(int identifier){
		for(ProtocolEntityType t : values()){
			if(t.getIdentifier() == identifier)
				return t;
		}
		throw new RuntimeException("Couldn't get entity type from id='" + identifier + "'.");
	}

}
