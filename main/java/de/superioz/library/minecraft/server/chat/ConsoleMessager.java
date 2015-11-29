package de.superioz.library.minecraft.server.chat;

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

    public void write(String message){
        this.write(message, false);
    }

    public void write(String message, boolean spacePrefix){
        if(!super.callEvent(this, message, getChannelTarget())){
            Bukkit.getConsoleSender().sendMessage(getMessage(message, spacePrefix));
        }
    }

}
