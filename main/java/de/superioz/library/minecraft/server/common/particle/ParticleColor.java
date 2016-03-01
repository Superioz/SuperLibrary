package de.superioz.library.minecraft.server.common.particle;

import org.bukkit.Color;

/**
 * Author @darkblade12
 */
public abstract class ParticleColor {

	/**
	 * Returns the value for the offsetX field
	 *
	 * @return The offsetX value
	 */
	public abstract float getValueX();

	/**
	 * Returns the value for the offsetY field
	 *
	 * @return The offsetY value
	 */
	public abstract float getValueY();

	/**
	 * Returns the value for the offsetZ field
	 *
	 * @return The offsetZ value
	 */
	public abstract float getValueZ();


	public static final class OrdinaryColor extends ParticleColor {
		private final int red;
		private final int green;
		private final int blue;

		/**
		 * Construct a new ordinary color
		 *
		 * @param red   Red value of the RGB format
		 * @param green Green value of the RGB format
		 * @param blue  Blue value of the RGB format
		 *
		 * @throws IllegalArgumentException If one of the values is lower than 0 or higher than 255
		 */
		public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException{
			if(red < 0){
				throw new IllegalArgumentException("The red value is lower than 0");
			}
			if(red > 255){
				throw new IllegalArgumentException("The red value is higher than 255");
			}
			this.red = red;
			if(green < 0){
				throw new IllegalArgumentException("The green value is lower than 0");
			}
			if(green > 255){
				throw new IllegalArgumentException("The green value is higher than 255");
			}
			this.green = green;
			if(blue < 0){
				throw new IllegalArgumentException("The blue value is lower than 0");
			}
			if(blue > 255){
				throw new IllegalArgumentException("The blue value is higher than 255");
			}
			this.blue = blue;
		}

		/**
		 * Construct a new ordinary color
		 *
		 * @param color Bukkit color
		 */
		public OrdinaryColor(Color color){
			this(color.getRed(), color.getGreen(), color.getBlue());
		}

		/**
		 * Returns the red value of the RGB format
		 *
		 * @return The red value
		 */
		public int getRed(){
			return red;
		}

		/**
		 * Returns the green value of the RGB format
		 *
		 * @return The green value
		 */
		public int getGreen(){
			return green;
		}

		/**
		 * Returns the blue value of the RGB format
		 *
		 * @return The blue value
		 */
		public int getBlue(){
			return blue;
		}

		/**
		 * Returns the red value divided by 255
		 *
		 * @return The offsetX value
		 */
		@Override
		public float getValueX(){
			return (float) red / 255F;
		}

		/**
		 * Returns the green value divided by 255
		 *
		 * @return The offsetY value
		 */
		@Override
		public float getValueY(){
			return (float) green / 255F;
		}

		/**
		 * Returns the blue value divided by 255
		 *
		 * @return The offsetZ value
		 */
		@Override
		public float getValueZ(){
			return (float) blue / 255F;
		}
	}

	public static final class NoteColor extends ParticleColor {
		private final int note;

		/**
		 * Construct a new note color
		 *
		 * @param note Note id which determines color
		 *
		 * @throws IllegalArgumentException If the note value is lower than 0 or higher than 24
		 */
		public NoteColor(int note) throws IllegalArgumentException{
			if(note < 0){
				throw new IllegalArgumentException("The note value is lower than 0");
			}
			if(note > 24){
				throw new IllegalArgumentException("The note value is higher than 24");
			}
			this.note = note;
		}

		/**
		 * Returns the note value divided by 24
		 *
		 * @return The offsetX value
		 */
		@Override
		public float getValueX(){
			return (float) note / 24F;
		}

		/**
		 * Returns zero because the offsetY value is unused
		 *
		 * @return zero
		 */
		@Override
		public float getValueY(){
			return 0;
		}

		/**
		 * Returns zero because the offsetZ value is unused
		 *
		 * @return zero
		 */
		@Override
		public float getValueZ(){
			return 0;
		}

	}

}
