package de.superioz.library.minecraft.server.message;

import org.bukkit.Bukkit;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class ConsoleMessager extends ChatMessageChannel {

    public ConsoleMessager(String prefix){
        super(prefix, Target.CONSOLE);
    }

    /**
     * Only important for me tho
     */
    public void write(String message){
        this.write(message, false);
    }

    /**
     * Only important for me tho
     */
    public void write(String message, boolean spacePrefix){
        if(!super.callEvent(this, message, getChannelTarget())){
            Bukkit.getConsoleSender().sendMessage(getMessage(message, spacePrefix));
        }
    }

}
