package de.superioz.library.bukkit.common.npc;

import de.superioz.library.java.util.ReflectionUtils;
import net.minecraft.server.v1_9_R1.PathEntity;
import net.minecraft.server.v1_9_R1.PathPoint;
import org.bukkit.Location;

/**
 * This class was created as a part of TestPlugin
 *
 * @author Superioz
 */
public class WrappedEntityPath extends PathEntity {

    private Location goal;
    private static final String FIELD_PATHPOINTS = "a";
    private static final String FIELD_COUNTER = "b";
    private static final String FIELD_LENGTH = "c";

    /**
     * Wrapper class of the pathEntity class
     *
     * @param pathPoints Pathpoints are like waypoints
     * @param to end location
     */
    public WrappedEntityPath(PathPoint[] pathPoints, Location to){
        super(pathPoints);
        this.goal = to;
    }

    /**
     * @return The path field of super class
     */
    public PathPoint[] getPath(){
        return (PathPoint[]) ReflectionUtils.getField(getClass().getSuperclass(), this, FIELD_PATHPOINTS);
    }

    /**
     * @return The counter field of super class
     */
    public int getCounter(){
        return (int) ReflectionUtils.getField(getClass().getSuperclass(), this, FIELD_COUNTER);
    }

    /**
     * @return The length field of super class
     */
    public int getLength(){
        return (int) ReflectionUtils.getField(getClass().getSuperclass(), this, FIELD_LENGTH);
    }

    public static WrappedEntityPath from(PathEntity entity, Location to){
        return new WrappedEntityPath((PathPoint[]) ReflectionUtils.getField(entity.getClass(),
                entity, FIELD_PATHPOINTS), to);
    }

    // Getter method because sometimes lombok sucks
    public Location getGoal(){
        return goal;
    }

}
