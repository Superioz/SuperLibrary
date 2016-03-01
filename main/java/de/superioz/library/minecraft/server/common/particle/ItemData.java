package de.superioz.library.minecraft.server.common.particle;

import org.bukkit.Material;

/**
 *
 */
public final class ItemData extends ParticleData {
	/**
	 * Construct a new item data
	 *
	 * @param material Material of the item
	 * @param data     Data value of the item
	 *
	 * @see ParticleData#ParticleData(Material, byte)
	 */
	public ItemData(Material material, byte data){
		super(material, data);
	}
}
