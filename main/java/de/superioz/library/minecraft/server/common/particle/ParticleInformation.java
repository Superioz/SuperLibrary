package de.superioz.library.minecraft.server.common.particle;

import org.bukkit.Location;

/**
 * Created on 02.02.2016.
 */
public class ParticleInformation {

	private float x;
	private float y;
	private float z;
	private Location loc;
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private int amount;
	private float speed;
	private ParticleData data;
	private ParticleEffect effect;

	// Constructor
	public ParticleInformation(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount,
			ParticleData data, ParticleEffect effect){
		if(speed < 0){
			throw new IllegalArgumentException("The speed is lower than 0");
		}
		if(amount < 0){
			throw new IllegalArgumentException("The amount is lower than 0");
		}

		this.loc = center;
		this.x = (float) loc.getX();
		this.y = (float) loc.getY();
		this.z = (float) loc.getZ();
		this.offsetY = offsetY;
		this.offsetX = offsetX;
		this.offsetZ = offsetZ;
		this.data = data;
		this.amount = amount;
		this.speed = speed;
		this.effect = effect;

	}

	public ParticleInformation(Location center, float offset, float speed, int amount, ParticleData data,
			ParticleEffect effect){
		this(center, offset, offset, offset, speed, amount, data, effect);
	}

	public ParticleInformation(Location center, float speed, int amount, ParticleData data, ParticleEffect effect){
		this(center, 0, speed, amount, data, effect);
	}

	public ParticleInformation(Location center, int amount, ParticleData data, ParticleEffect effect){
		this(center, 0, amount, data, effect);
	}

	public ParticleInformation(Location center, int amount, ParticleEffect effect){
		this(center, amount, null, effect);
	}

	public ParticleInformation(Location center, ParticleColor color, ParticleEffect effect){
		this(center, color.getValueX(), color.getValueY(), color.getValueZ(), 1, 1, null, effect);
		if(effect == ParticleEffect.RED_DUST && color instanceof ParticleColor.OrdinaryColor &&
				((ParticleColor.OrdinaryColor) color).getRed() == 0){
			offsetX = Float.MIN_NORMAL;
		}
	}


	// -- Intern methods

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public float getZ(){
		return z;
	}

	public float getOffsetX(){
		return offsetX;
	}

	public float getOffsetY(){
		return offsetY;
	}

	public float getOffsetZ(){
		return offsetZ;
	}

	public int getAmount(){
		return amount;
	}

	public ParticleData getData(){
		return data;
	}

	public Location getCenter(){
		return loc;
	}

	public float getSpeed(){
		return speed;
	}

	public ParticleEffect getEffect(){
		return effect;
	}

	public boolean isLongDistance(){
		return true;
	}

}
