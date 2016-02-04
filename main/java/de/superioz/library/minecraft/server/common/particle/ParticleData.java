package de.superioz.library.minecraft.server.common.particle;

import org.bukkit.Material;

/**
 * Created on 04.02.2016.
 */
public abstract class ParticleData {

	private final Material material;
	private final byte data;
	private final int[] packetData;

	/**
	 * Construct a new particle data
	 *
	 * @param material Material of the item/block
	 * @param data     Data value of the item/block
	 */
	@SuppressWarnings("deprecation")
	public ParticleData(Material material, byte data){
		this.material = material;
		this.data = data;
		this.packetData = new int[]{material.getId(), data};
	}

	/**
	 * Returns the material of this data
	 *
	 * @return The material
	 */
	public Material getMaterial(){
		return material;
	}

	/**
	 * Returns the data value of this data
	 *
	 * @return The data value
	 */
	public byte getData(){
		return data;
	}

	/**
	 * Returns the data as an int array for packet construction
	 *
	 * @return The data for the packet
	 */
	public int[] getPacketData(){
		return packetData;
	}

}
