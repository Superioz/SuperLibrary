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

    public void load(boolean copyDefaults){
        super.load(copyDefaults, true);

        try{
            this.properties.load(new FileInputStream(super.getFile()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Properties getProperties(){
        return properties;
    }

    @SuppressWarnings("unchecked")
    public E get(String key){
        if(filter != null)
            return (E) filter.filter(properties.get(key));
        else
            return (E) properties.get(key);
    }

    public void set(String key, E object){
        properties.setProperty(key, String.valueOf(object));
    }

    public void applyFilter(PropertyFilter<E> filter){
        this.filter = filter;
    }

}
