package de.superioz.library.minecraft.server.chat;

import de.superioz.library.minecraft.server.util.BukkitUtil;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class PlayerMessager extends ChatMessageChannel {

    public PlayerMessager(String prefix){
        super(prefix, Target.PLAYER);
    }

    public void write(String message, Player... players){
        this.write(message, false, players);
    }

    public void write(String message, boolean spacePrefix, Player... players){
        if(!super.callEvent(this, message, getChannelTarget(), BukkitUtil.onlinePlayers())){
            for(Player p : players)
                p.sendMessage(getMessage(message, spacePrefix));
        }
    }

    public void broadcast(String message){
        this.broadcast(message, false);
    }

    public void broadcast(String message, boolean spacePrefix){
        if(!super.callEvent(this, message, getChannelTarget(), BukkitUtil.onlinePlayers())){
            for(Player p : BukkitUtil.onlinePlayers())
                p.sendMessage(getMessage(message, spacePrefix));
        }
    }

}
