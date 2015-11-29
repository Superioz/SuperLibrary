package de.superioz.library.java.cache;

import de.superioz.library.java.file.type.JsonFile;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public interface Cache {

    void write(JsonFile file);

    void from(JsonFile file);

}
