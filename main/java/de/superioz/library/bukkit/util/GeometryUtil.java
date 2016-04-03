package de.superioz.library.bukkit.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class GeometryUtil {

    /**
     * Calculate a helix
     *
     * @param loc    Middle location
     * @param radius The radius
     * @param height The height
     * @param space  The space between every block
     *
     * @return The list of locations
     */
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

    /**
     * Calculate a cuboid
     *
     * @param pos1 Position one
     * @param pos2 Position two
     *
     * @return The list of locations
     */
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

        for(int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); x++){
            for(int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY() && y <= world.getMaxHeight(); y++){
                for(int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); z++){
                    blockList.add(world.getBlockAt(x, y, z).getLocation());
                }
            }
        }

        return blockList;
    }

    /**
     * Calculate a sphere
     *
     * @param loc    Middle location
     * @param radius The radius
     * @param hollow Hollow?
     *
     * @return The set of locations
     */
    public static List<Location> calcSphere(Location loc, int radius, boolean hollow){
        return circle(loc, radius, radius, hollow, true);
    }

    /**
     * Calculate a circle
     *
     * @param loc    The middle location
     * @param radius The radius
     * @param height The height
     * @param hollow Hollow?
     *
     * @return The list of locations
     */
    public static List<Location> calcCircle(Location loc, int radius, int height, boolean hollow){
        return circle(loc, radius, height, hollow, false);
    }

    /**
     * Uses the fill4 algorithm
     *
     * @param middle The middle
     * @param type   The type of surface
     *
     * @return The list of locations
     */
    public static Set<Block> fill4(Location middle, Material type){
        Block b = middle.getBlock();
        return fill4(b.getWorld(), b.getX(), b.getY(), b.getZ(), type, false);
    }

    /**
     * Uses the fill4 algorithm
     *
     * @param world   The world
     * @param x       X location
     * @param y       Y location
     * @param z       Z location
     * @param oldType Old type
     * @param flipped Flipped? (border or area)
     *
     * @return The list of locations
     */
    public static Set<Block> fill4(World world, int x, int y, int z, Material oldType, boolean flipped){
        Stack<SimpleBlock> stack = new Stack<>();
        Set<Block> blockSet = new HashSet<>();

        stack.push(new SimpleBlock(x, z));

        while(!stack.empty()){
            SimpleBlock block = stack.pop();
            Block b = world.getBlockAt(block.getX(), y, block.getZ());

            if(blockSet.size() >= MAX_FLOODFILL_SIZE)
                break;

            if(!blockSet.contains(b) && (flipped ? b.getType() != oldType : b.getType() == oldType)){
                blockSet.add(world.getBlockAt(block.getX(), y, block.getZ()));

                stack.push(new SimpleBlock(block.getX(), block.getZ() + 1));
                stack.push(new SimpleBlock(block.getX(), block.getZ() - 1));
                stack.push(new SimpleBlock(block.getX() + 1, block.getZ()));
                stack.push(new SimpleBlock(block.getX() - 1, block.getZ()));
            }
        }
        return blockSet;
    }

    // -- Intern methods

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

    public static final int MAX_FLOODFILL_SIZE = Integer.MAX_VALUE;

    private static class SimpleBlock {

        private int x;
        private int z;

        public SimpleBlock(int x, int z){
            this.x = x;
            this.z = z;
        }

        public int getX(){
            return x;
        }

        public int getZ(){
            return z;
        }

    }

}
