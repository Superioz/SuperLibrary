package de.superioz.library.minecraft.server.common.particle;

import org.bukkit.Material;

/**
 *
 */
public final class BlockData extends ParticleData {
	/**
	 * Construct a new block data
	 *
	 * @param material Material of the block
	 * @param data     Data value of the block
	 *
	 * @throws IllegalArgumentException If the material is not a block
	 * @see ParticleData#ParticleData(Material, byte)
	 */
	public BlockData(Material material, byte data) throws IllegalArgumentException{
		super(material, data);
		if(!material.isBlock()){
			throw new IllegalArgumentException("The material is not a block");
		}
	}
}
