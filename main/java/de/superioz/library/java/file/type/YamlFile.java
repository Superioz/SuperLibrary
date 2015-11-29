package de.superioz.library.java.file.type;

import de.superioz.library.java.file.parent.CustomFile;
import de.superioz.library.java.file.parent.SupportedFiletype;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Class created on April in 2015
 */
public class YamlFile extends CustomFile {

    protected FileConfiguration configuration;

    public YamlFile(String filename, String extraPath, File root){
        super(filename, extraPath, root, SupportedFiletype.YAML);
    }

    @Override
    public void load(boolean copyDefaults, boolean create){
        super.load(copyDefaults, create);
        this.configuration = YamlConfiguration.loadConfiguration(super.file);
    }

    public void save() {
        try{
            this.configuration.save(super.file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration config(){
        return configuration;
    }

}
