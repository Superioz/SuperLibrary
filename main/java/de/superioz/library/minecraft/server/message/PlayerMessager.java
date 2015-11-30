package de.superioz.library.minecraft.server.message;

import de.superioz.library.minecraft.server.common.view.ViewManager;
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
        this.write(message, spacePrefix, MessageChannel.CHAT, players);
    }

    public void write(String message, boolean spacePrefix, MessageChannel channel, Player... players){
        if(!super.callEvent(this, message, getChannelTarget(), BukkitUtil.onlinePlayers()))
            return;

        switch(channel){
            case CHAT:
                for(Player p : players)
                    p.sendMessage(getMessage(message, spacePrefix));
                break;
            case ACTIONBAR:
                ViewManager.sendHotbarMessage(getMessage(message, spacePrefix), players);
                break;
            case TITLE:
                for(Player p : players)
                    ViewManager.sendTitle(p, getMessage(message, spacePrefix));
                break;
        }
    }

    public void broadcast(String message){
        this.broadcast(message, false);
    }

    public void broadcast(String message, boolean spacePrefix){
        this.broadcast(message, spacePrefix, MessageChannel.CHAT);
    }

    public void broadcast(String message, boolean spacePrefix, MessageChannel channel){
        this.write(message, spacePrefix, channel, BukkitUtil.onlinePlayers());
    }

}
