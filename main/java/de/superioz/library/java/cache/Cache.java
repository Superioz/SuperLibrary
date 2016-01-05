package de.superioz.library.java.cache;

import de.superioz.library.java.file.type.JsonFile;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public interface Cache {

    /**
     * Write cache to given file
     *
     * @param file The file
     */
    void write(JsonFile file);

    /**
     * Get cache from given file
     *
     * @param file The file
     */
    void from(JsonFile file);

}
