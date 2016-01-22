package de.superioz.library.java.file.properties;

import de.superioz.library.java.file.type.PropertiesFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SuperProperties<E> extends PropertiesFile {

    private Properties properties;
    private PropertyFilter filter;

    public SuperProperties(String filename, String extraPath, File root){
        super(filename, extraPath, root);
        properties = new Properties();
    }

    /**
     * Loads the properties
     *
     * @param copyDefaults Should defaults be copied into the file
     */
    public void load(boolean copyDefaults){
        super.load(copyDefaults, true);

        try{
            this.properties.load(new FileInputStream(super.getFile()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Gets object from given key
     *
     * @param key The key
     *
     * @return The object
     */
    @SuppressWarnings("unchecked")
    public E get(String key){
        if(filter != null)
            return (E) filter.filter(properties.get(key));
        else
            return (E) properties.get(key);
    }

    /**
     * Set object to file with given key
     *
     * @param key    The key
     * @param object The object
     */
    public void set(String key, E object){
        properties.setProperty(key, String.valueOf(object));
    }

    /**
     * Applies filter to this file
     *
     * @param filter The filter
     */
    public void applyFilter(PropertyFilter<E> filter){
        this.filter = filter;
    }

    // -- Intern methods

    public Properties getProperties(){
        return properties;
    }

}
