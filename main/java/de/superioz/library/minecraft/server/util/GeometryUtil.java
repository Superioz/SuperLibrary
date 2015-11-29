package de.superioz.library.minecraft.server.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class GeometryUtil {

    public static List<Location> calcHelix(Location loc, int radius, int height, double space){
        List<Location> locations = new ArrayList<>();

        for(double y = 0; y <= height; y += space){
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);

            locations.add(new Location(loc.getWorld(),
                    (float) (loc.getX() + x),
                    (float) (loc.getY() + y),
                    (float) (loc.getZ() + z)));
        }
        return locations;
    }

    public static List<Location> calcCuboid(Location pos1, Location pos2){
        List<Location> blockList = new ArrayList<>();

        double x1 = pos1.getX();
        double x2 = pos2.getX();
        double y1 = pos1.getY();
        double y2 = pos2.getY();
        double z1 = pos1.getZ();
        double z2 = pos2.getZ();

        double xPos1 = Math.min(x1, x2);
        double xPos2 = Math.max(x1, x2);
        double yPos1 = Math.min(y1, y2);
        double yPos2 = Math.max(y1, y2);
        double zPos1 = Math.min(z1, z2);
        double zPos2 = Math.max(z1, z2);

        Vector minimumPoint = new Vector(xPos1, yPos1, zPos1);
        Vector maximumPoint = new Vector(xPos2, yPos2, zPos2);

        World world = pos1.getWorld();

        for (int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); x++) {
            for (int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY() && y <= world.getMaxHeight(); y++) {
                for (int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); z++) {
                    blockList.add(world.getBlockAt(x, y, z).getLocation());
                }
            }
        }

        return blockList;
    }

    public static List<Location> calcSphere(Location loc, int radius, boolean hollow){
        return circle(loc, radius, radius, hollow, true);
    }

    public static List<Location> calcCircle(Location loc, int radius, int height, boolean hollow){
        return circle(loc, radius, height, hollow, false);
    }

    // ===============================================================================================================

    private static List<Location> circle(Location origin, int radius, int height, boolean hollow, boolean sphere){
        List<Location> blocks = new ArrayList<>();
        int cx = origin.getBlockX(), cy = origin.getBlockY(), cz = origin.getBlockZ();
        for(int x = cx - radius; x <= cx + radius; x++){
            for(int z = cz - radius; z <= cz + radius; z++){
                for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++){
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))){
                        Location l = new Location(origin.getWorld(), x, y, z);
                        blocks.add(l);
                    }
                }
            }
        }
        return blocks;
    }

}
