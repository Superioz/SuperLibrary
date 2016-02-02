package de.superioz.library.minecraft.server.message;

import de.superioz.library.minecraft.server.common.ViewManager;
import de.superioz.library.minecraft.server.util.BukkitUtilities;
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

    /**
     * Only important for me tho
     */
    public void write(String message, Player... players){
        this.write(message, false, players);
    }

    /**
     * Only important for me tho
     */
    public void write(String message, boolean spacePrefix, Player... players){
        this.write(message, spacePrefix, MessageChannel.CHAT, players);
    }

    /**
     * Only important for me tho
     */
    public void write(String message, boolean spacePrefix, MessageChannel channel, Player... players){
        if(!super.callEvent(this, message, getChannelTarget(), BukkitUtilities.onlinePlayers()))
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
                    ViewManager.sendTitle(getMessage(message, spacePrefix), p);
                break;
        }
    }

    /**
     * Only important for me tho
     */
    public void broadcast(String message){
        this.broadcast(message, false);
    }

    /**
     * Only important for me tho
     */
    public void broadcast(String message, boolean spacePrefix){
        this.broadcast(message, spacePrefix, MessageChannel.CHAT);
    }

    /**
     * Only important for me tho
     */
    public void broadcast(String message, boolean spacePrefix, MessageChannel channel){
        this.write(message, spacePrefix, channel, BukkitUtilities.onlinePlayers());
    }

}
