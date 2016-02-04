package de.superioz.library.minecraft.server.util;

import de.superioz.library.java.util.list.ListUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

/**
 * Class created on April in 2015
 */
public class LocationUtil {

	/**
	 * Intern used method
	 */
	public static int toFixedPoint(double value){
		return (int) ((value) * 32D);
	}

	/**
	 * Intern used method
	 */
	public static double toAngle(double value){
		return value * 256.0F / 360.0F;
	}

	/**
	 * Intern used method
	 */
	public static int toVelocity(double value){
		return (int) (value * 8000.0D);
	}

	/**
	 * Intern used method
	 */
	public static Location fix(Location loc){
		return loc.getBlock().getLocation().clone().add(0.5, 0, 0.5);
	}

	/**
	 * Intern used method
	 */
	public static boolean isSimilar(Location l1, Location l2){
		return !(l1 == null || l2 == null)
				&& l1.getBlock().getLocation().equals(l2.getBlock().getLocation());
	}

	/**
	 * Intern used method
	 */
	public static boolean isSimilar(List<Location> locs1, List<Location> locs2){
		if(locs1 == null || locs2 == null) return false;
		if(locs1.size() != locs2.size()) return false;

		for(int i = 0; i < locs1.size(); i++){
			if(!isSimilar(locs1.get(i), locs2.get(i))){
				return false;
			}
		}
		return true;
	}

	/**
	 * Intern used method
	 */
	public static String toString(Location l){
		String[] ar = new String[]{l.getBlockX() + "", l.getBlockY() + "", l.getBlockZ() + ""};
		return ListUtil.insert(ar, ",");
	}

	/**
	 * Calculate angle between two points to set head and body rotation
	 *
	 * @param point1 Vector of location from
	 * @param point2 Vector of location to
	 *
	 * @return The angle as a float
	 */
	public static float getLocalAngle(Vector point1, Vector point2){
		double dx = point2.getX() - point1.getX();
		double dz = point2.getZ() - point1.getZ();
		float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
		if(angle < 0){
			angle += 360.0F;
		}
		return angle;
	}

	/**
	 * Checks if the realVal is distanceVal +/- aboutVal
	 *
	 * @param realVal     The real range
	 * @param distanceVal The best distance
	 * @param aboutVal    The +/- val
	 *
	 * @return The result
	 */
	public static boolean checkRoughRange(double realVal, double distanceVal, double aboutVal){
		return (realVal >= distanceVal - aboutVal) && (realVal <= distanceVal + aboutVal);
	}

	/**
	 * Checks if given location is given material
	 *
	 * @param loc       The loc
	 * @param materials The material
	 *
	 * @return The result
	 */
	public static boolean isMaterial(Location loc, Material... materials){
		Material type = loc.getBlock().getType();
		return Arrays.asList(materials).contains(type);
	}

}
