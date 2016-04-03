package de.superioz.library.bukkit.util;

import de.superioz.library.java.file.parent.CustomFile;
import de.superioz.library.java.file.parent.SupportedFiletype;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created on 16.03.2016.
 */
public class YamlFile extends CustomFile {

	protected FileConfiguration configuration;

	public YamlFile(String filename, String extraPath, File root){
		super(filename, extraPath, root, SupportedFiletype.YAML);
	}

	/**
	 * Saves this file
	 */
	public void save() {
		try{
			this.configuration.save(super.file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	// -- Intern methods

	public FileConfiguration config(){
		return configuration;
	}

	@Override
	public void load(boolean copyDefaults, boolean create){
		super.load(copyDefaults, create);
		this.configuration = YamlConfiguration.loadConfiguration(super.file);
	}

}
