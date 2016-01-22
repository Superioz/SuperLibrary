package de.superioz.library.minecraft.server.lab.packet;


import java.util.HashSet;

/**
 * Class created on April in 2015
 */
public class EntityIDManager {

    /**
     * Default id is the first id
     */
    public static final int DEFAULT_ID = 6900;

    /**
     * All reserved id's
     * @see #DEFAULT_ID
     * @see #reserve(Integer)
     */
    private static HashSet<Integer> entityIDs = new HashSet<>();

    /**
     * Get all id's
     * @see #entityIDs
     *
     * @return A {@link HashSet} with all {@link #entityIDs}
     */
    public static HashSet<Integer> getIDs(){
        return entityIDs;
    }

    /**
     * Checks if the id is reserver
     * @see #getIDs()
     * @see #entityIDs
     *
     * @return If the check if right
     */
    public static boolean isReserved(int value){
        return entityIDs.contains(value);
    }

    /**
     * Deletes a reservation
     * @return The reserved id
     */
    public static int reserve(Integer id){
        int c = id;
        while(isReserved(DEFAULT_ID + c)){
            c++;
        }

        entityIDs.add(c + DEFAULT_ID);
        return c + DEFAULT_ID;
    }

    /**
     * Reserves a complete new id
     * @see #delReservation(Integer)
     * @see #reserve(Integer)
     *
     * @param id The id from {@link #entityIDs}
     * @return A new reserved if of {@link #entityIDs}
     */
    public static int reserveNew(Integer id){
        if(isReserved(id)){
            delReservation(id);
            return reserve(id);
        }

        return id;
    }

    /**
     * Deletes a reservation
     */
    public static void delReservation(Integer id){
        if(isReserved(DEFAULT_ID+id))
            entityIDs.remove(DEFAULT_ID+id);
    }

}
