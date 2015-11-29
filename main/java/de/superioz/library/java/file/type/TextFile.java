package de.superioz.library.java.file.type;

import de.superioz.library.java.file.parent.CustomFile;
import de.superioz.library.java.file.parent.SupportedFiletype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Class created on April in 2015
 */
public class TextFile extends CustomFile {

    /**
     * Constructor of kind file
     *
     * @param filename Name of file {@link super#filename}
     */
    public TextFile(String filename, String extraPath, File root){
        super(filename, extraPath, root, SupportedFiletype.TEXT);
    }

    /**
     * Loads this file
     * @see super#load(boolean, boolean)
     *
     * @param copyDefaults If there is a default file
     * @param create If it should directly create
     */
    @Override
    public void load(boolean copyDefaults, boolean create){
        super.load(copyDefaults, true);
    }

    /**
     * Writes to {@link super#file} WITHOUT auto backslash
     * @see PrintWriter#print(String)
     *
     * @param strings The strings to write into
     */
    public void write(String... strings){
        try{
            PrintWriter writer = new PrintWriter(super.file, "UTF-8");

            for(String s : strings){
                writer.print(s);
            }
            writer.close();
        }catch(FileNotFoundException
                | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    /**
     * Writes to {@link super#file} WITH auto backslash
     * @see PrintWriter#println(String)
     *
     * @param strings The strings to write into
     */
    public void writeln(String... strings){
        try{
            PrintWriter writer = new PrintWriter(super.file, "UTF-8");

            for(String s : strings){
                writer.println(s);
            }
            writer.close();
        }catch(FileNotFoundException
                | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

}
