package de.superioz.library.java.file.type;

import com.google.gson.Gson;
import de.superioz.library.java.file.parent.CustomFile;
import de.superioz.library.java.file.parent.SupportedFiletype;

import java.io.*;
import java.lang.reflect.Type;

/**
 * Class created on April in 2015
 */
public class JsonFile extends CustomFile {

    protected Gson gsonLibrary;

    public JsonFile(String filename, String extraPath, File root){
        super(filename, extraPath, root, SupportedFiletype.JSON);
        gsonLibrary = new Gson();
    }

    /**
     * Loads the file
     *
     * @param copyDefaults Should
     */
    public void load(boolean copyDefaults){
        super.load(copyDefaults, true);
    }

    public void write(Object obj){
        String json = gsonLibrary.toJson(obj);

        try{
            FileWriter writer = new FileWriter(super.file);
            writer.write(json);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads file with given master class (as type)
     *
     * @param clazz The class
     *
     * @return The object
     */
    @SuppressWarnings("unchecked")
    public <T> T read(Class clazz){
        try{
            BufferedReader br = new BufferedReader(new FileReader(super.file));
            return (T) gsonLibrary.fromJson(br, clazz);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads file with given type
     *
     * @param type The type
     *
     * @return The object
     */
    @SuppressWarnings("unchecked")
    public <T> T read(Type type){
        try{
            BufferedReader br = new BufferedReader(new FileReader(super.file));
            return (T) gsonLibrary.fromJson(br, type);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

}
