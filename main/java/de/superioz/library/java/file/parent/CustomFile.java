package de.superioz.library.java.file.parent;

import java.io.*;

/**
 * Class created on April in 2015
 */
public abstract class CustomFile {

    protected String filename;
    protected File file;
    protected SupportedFiletype filetype;
    protected boolean loaded = false;

    public CustomFile(String filename, String extraPath, File root, SupportedFiletype filetype){
        String pathAdd = extraPath;
        this.filetype = filetype;
        this.filename = filename + "." + filetype.getName();

        if(!pathAdd.isEmpty())
            pathAdd = "/" + pathAdd + "/";
        file = new File(root + pathAdd, this.filename);
    }

    /**
     * Takes a file with the same name as this file and copies the content from it
     *
     * @param in   The file
     * @param file This file
     */
    private void copyDefaultsFrom(InputStream in, File file){
        if(in == null
                || file == null)
            return;

        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;

            while((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }

            out.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Loads this file
     *
     * @param copyDefaults If defaults should be copied into this file
     * @param create       If the file should also be created
     */
    public void load(boolean copyDefaults, boolean create){
        if(!file.exists()){
            file.getParentFile().mkdirs();

            if(copyDefaults)
                this.copyDefaultsFrom(getClass().getResourceAsStream("/" + this.filename), file);

            // Create
            if(create)
                try{
                    file.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }

            if(file.exists())
                loaded = true;
        }
    }

    /**
     * Deletes this file
     *
     * @return If the deletion was successful
     */
    protected boolean delete(){
        return this.file.delete();
    }

    // -- Intern methods

    public String getFilename(){
        return filename;
    }

    public File getFile(){
        return file;
    }

    public SupportedFiletype getFiletype(){
        return filetype;
    }

    public boolean isLoaded(){
        return loaded;
    }

    public boolean exists(){
        return file.exists();
    }

}
