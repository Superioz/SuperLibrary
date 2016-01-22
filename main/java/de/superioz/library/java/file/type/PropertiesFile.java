package de.superioz.library.java.file.type;

import de.superioz.library.java.file.parent.CustomFile;
import de.superioz.library.java.file.parent.SupportedFiletype;

import java.io.File;

/**
 * This class was created as a part of GunGame (Spigot)
 *
 * @author Superioz
 */
public class PropertiesFile extends CustomFile {

    /**
     * Constructor of this fileWrapper
     *
     * @param filename  {@link #filename}
     * @param extraPath Extrapath from the plugin folder like "folder/extra/path"
     */
    public PropertiesFile(String filename, String extraPath, File root){
        super(filename, extraPath, root, SupportedFiletype.PROPERTIES);
    }

    // -- Intern methods

    @Override
    public void load(boolean copyDefaults, boolean create){
        super.load(copyDefaults, true);
    }
}
