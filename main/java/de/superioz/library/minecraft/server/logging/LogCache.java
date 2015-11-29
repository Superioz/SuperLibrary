package de.superioz.library.minecraft.server.logging;

import de.superioz.library.java.util.TimeUtils;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class created on April in 2015
 */
public class LogCache {

    protected List<String> lines;
    protected long startTimestamp;
    protected long endTimestamp;
    protected Plugin plugin;
    protected String folder;

    public LogCache(Plugin plugin, String folder){
        this.lines = new ArrayList<>();
        this.plugin = plugin;
        this.folder = folder;

        if(!folder.isEmpty()){
            this.folder = "/" + folder;
        }

        startTimestamp = TimeUtils.timestamp();
        lines.add("# Start logfile @" + startTimestamp);
    }

    public LogCache log(String msg){
        lines.add("[" + TimeUtils.getCurrentTime() + "]: " + msg);
        return this;
    }

    public void build(){
        endTimestamp = TimeUtils.timestamp();

        String fileName = TimeUtils.getCurrentTime("dd-MM-YYYY_HH-mm-ss");
        File f = new File(plugin.getDataFolder() + folder, fileName + ".log");

        if(!f.exists()){
            f.getParentFile().mkdirs();
            try{
                f.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        // Set text of file
        try{
            PrintWriter writer = new PrintWriter(f, "UTF-8");

            lines.forEach(writer::println);
            writer.println("# End logfile @" + endTimestamp);
            writer.close();
        }catch(FileNotFoundException
                | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public long end(){
        return endTimestamp;
    }

    public long start(){
        return startTimestamp;
    }
}
