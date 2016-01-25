package de.superioz.library.java.util;

import java.util.Random;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class RandomUtil {

    private static Random r = new Random();

    /**
     * Gets an integer between two values
     *
     * @param from The min
     * @param to   The max
     *
     * @return The random number
     */
    public static int getInteger(int from, int to){
        return ((int) (Math.random() * (to - from))) + from;
    }

    /**
     * Gets a double between two values
     *
     * @param from The min
     * @param to   The max
     *
     * @return The random number
     */
    public static double getDouble(int from, int to){
        return ((Math.random() * (to - from))) + from;
    }

    /**
     * Gets a random boolean
     *
     * @return The boolean
     */
    public static boolean getBoolean(){
        return r.nextBoolean();
    }


}
