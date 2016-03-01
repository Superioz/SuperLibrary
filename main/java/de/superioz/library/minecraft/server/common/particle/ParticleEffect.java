package de.superioz.library.minecraft.server.common.particle;

import de.superioz.library.minecraft.server.util.LocationUtil;
import de.superioz.library.minecraft.server.util.protocol.BukkitPackets;
import de.superioz.library.minecraft.server.util.protocol.CraftBukkitUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 02.02.2016.
 */
public enum ParticleEffect {

	EXPLODE("explode", 0, ParticleProperty.DIRECTIONAL),
	LARGE_EXPLODE("largeexplode", 1),
	HUGE_EXPLOSION("hugeexplosion", 2),
	FIREWORKS_SPARK("fireworksSpark", 3, ParticleProperty.DIRECTIONAL),
	BUBBLE("bubble", 4, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
	SPLASH("splash", 5, ParticleProperty.DIRECTIONAL),
	WAKE("wake", 6, ParticleProperty.DIRECTIONAL),
	SUSPENDED("suspended", 7, ParticleProperty.REQUIRES_WATER),
	DEPTH_SUSPENDED("depthsuspend", 8, ParticleProperty.DIRECTIONAL),
	CRIT("crit", 9, ParticleProperty.DIRECTIONAL),
	MAGIC_CRIT("magicCrit", 10, ParticleProperty.DIRECTIONAL),
	SMOKE("smoke", 11, ParticleProperty.DIRECTIONAL),
	LARGE_SMOKE("largesmoke", 12, ParticleProperty.DIRECTIONAL),
	SPELL("spell", 13),
	INSTANT_SPELL("instantSpell", 14),
	MOB_SPELL("mobSpell", 15, ParticleProperty.COLORABLE),
	MOB_SPELL_AMBIENT("mobSpellAmbient", 16, ParticleProperty.COLORABLE),
	WITCH_MAGIC("witchMagic", 17),
	DRIP_WATER("dripWater", 18),
	DRIP_LAVA("dripLava", 19),
	ANGRY_VILLAGER("angryVillager", 20),
	HAPPY_VILLAGER("happyVillager", 21, ParticleProperty.DIRECTIONAL),
	TOWN_AURA("townaura", 22, ParticleProperty.DIRECTIONAL),
	NOTE("note", 23, ParticleProperty.COLORABLE),
	PORTAL("portal", 24, ParticleProperty.DIRECTIONAL),
	ENCHANTMENT_TABLE("enchantmenttable", 25, ParticleProperty.DIRECTIONAL),
	FLAME("flame", 26, ParticleProperty.DIRECTIONAL),
	LAVA("lava", 27),
	FOOTSTEP("footstep", 28),
	CLOUD("cloud", 29, ParticleProperty.DIRECTIONAL),
	RED_DUST("reddust", 30, ParticleProperty.COLORABLE),
	SNOWBALLPOOF("snowballpoof", 31),
	SNOWSHOVEL("snowshovel", 32, ParticleProperty.DIRECTIONAL),
	SLIME("slime", 33),
	HEART("heart", 34),
	BARRIER("barrier", 35),
	ICONCRACK("iconcrack", 36, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
	BLOCKCRACK("blockcrack", 37, ParticleProperty.REQUIRES_DATA),
	BLOCKDUST("blockdust", 38, ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
	DROPLET("droplet", 39),
	TAKE("take", 40),
	MOB_APPEARENCE("mobappearance", 41);

	public String name;
	public int id;
	public List<ParticleProperty> properties;

	ParticleEffect(String name, int id, ParticleProperty... properties){
		this.name = name;
		this.id = id;
		this.properties = Arrays.asList(properties);
	}

	/**
	 * Checks if the particle has given property
	 *
	 * @param property The property
	 *
	 * @return The result
	 */
	public boolean hasProperty(ParticleProperty property){
		return properties.contains(property);
	}

	/**
	 * Checks if the color type is correct
	 *
	 * @param effect The effect
	 * @param color  The color
	 *
	 * @return The result
	 */
	private static boolean isColorCorrect(ParticleEffect effect, ParticleColor color){
		return ((effect == SPELL || effect == MOB_SPELL_AMBIENT || effect == RED_DUST)
				&& color instanceof ParticleColor.OrdinaryColor)
				|| (effect == NOTE && color instanceof ParticleColor.NoteColor);
	}

	/**
	 * Checks if the data is correct
	 *
	 * @param effect The effect
	 * @param data   The data
	 *
	 * @return The result
	 */
	private static boolean isDataCorrect(ParticleEffect effect, ParticleData data){
		return ((effect == BLOCKCRACK || effect == BLOCKDUST)
				&& data instanceof BlockData)
				|| (effect == ICONCRACK && data instanceof ItemData);
	}

	/**
	 * Displays this particle effect with given properties
	 *
	 * @param offsetX Offset X
	 * @param offsetY Offset Y
	 * @param offsetZ Offset Z
	 * @param speed   The particle speed
	 * @param amount  The particle amount
	 * @param center  The center location
	 * @param players The viewers
	 */
	public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center,
			Player... players){
		if(hasProperty(ParticleProperty.REQUIRES_DATA)){
			throw new IllegalArgumentException("This particle effect requires additional data");
		}
		if(hasProperty(ParticleProperty.REQUIRES_WATER)
				&& !LocationUtil.isMaterial(center, Material.STATIONARY_WATER, Material.WATER)){
			throw new IllegalArgumentException("There is no water at the center location");
		}

		ParticleInformation information =
				new ParticleInformation(center, offsetX, offsetY, offsetZ, speed, amount, null, this);
	}

	/**
	 * Displays this particle effect with given properties
	 *
	 * @param direction The direction as vector
	 * @param speed     The speed
	 * @param center    The center location
	 * @param players   The viewers
	 */
	public void display(Vector direction, float speed, Location center, Player... players){
		if(hasProperty(ParticleProperty.REQUIRES_DATA)){
			throw new IllegalArgumentException("This particle effect requires additional data");
		}
		if(!hasProperty(ParticleProperty.DIRECTIONAL)){
			throw new IllegalArgumentException("This particle effect is not directional");
		}
		if(hasProperty(ParticleProperty.REQUIRES_WATER)
				&& !LocationUtil.isMaterial(center, Material.STATIONARY_WATER, Material.WATER)){
			throw new IllegalArgumentException("There is no water at the center location");
		}

		// Send packet
		ParticleInformation information =
				new ParticleInformation(center, (float) direction.getX(), (float) direction.getY(),
						(float) direction.getZ(), speed, 1, null, this);
	}

	/**
	 * Displays this particle effect with given properties
	 *
	 * @param color   The particle color
	 * @param center  The center
	 * @param players The viewers
	 */
	public void display(ParticleColor color, Location center, List<Player> players){
		if(!hasProperty(ParticleProperty.COLORABLE)){
			throw new IllegalArgumentException("This particle effect is not colorable");
		}
		if(!isColorCorrect(this, color)){
			throw new IllegalArgumentException("This particle type is not correct");
		}

		// Send packet
		ParticleInformation information =
				new ParticleInformation(center, color, this);

	}

	public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount,
			Location center, Player... players){
		if(!hasProperty(ParticleProperty.REQUIRES_DATA)){
			throw new IllegalArgumentException("This particle effect does not require additional data");
		}
		if(!isDataCorrect(this, data)){
			throw new IllegalArgumentException("The particle data type is incorrect");
		}

		// Send packet
		ParticleInformation information =
				new ParticleInformation(center, offsetX, offsetY, offsetZ, speed, amount, data, this);

	}

	// -- Intern methods

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public List<ParticleProperty> getProperties(){
		return properties;
	}

	/**
	 * Properties for particles
	 */
	public enum ParticleProperty {

		DIRECTIONAL,
		REQUIRES_WATER,
		COLORABLE,
		REQUIRES_DATA

	}

}
