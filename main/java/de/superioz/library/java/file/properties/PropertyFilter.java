package de.superioz.library.java.file.properties;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public abstract class PropertyFilter<E> {

    /**
     * Filters given object (edit or smth)
     * @param object The object (a string etc)
     * @return The object
     */
    public abstract E filter(E object);

}
