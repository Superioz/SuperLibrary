package de.superioz.library.bukkit.message;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.bukkit.event.ChatMessageChannelEvent;
import de.superioz.library.bukkit.util.ChatUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
@Getter
public abstract class ChatMessageChannel {

    protected Target channelTarget;
    protected String prefix;

    public ChatMessageChannel(String prefix, Target target){
        this.prefix = prefix;
        this.channelTarget = target;
    }

    /**
     * Only important for me tho
     */
    public String spacePrefix(boolean flag){
        return flag ? (prefix+" ") : prefix;
    }

    /**
     * Only important for me tho
     */
    protected String getMessage(String msg, boolean spacePrefix){
        return !spacePrefix ? ChatUtil.colored(prefix + msg) : ChatUtil.colored(prefix + " " + msg);
    }

    /**
     * Only important for me tho
     */
    protected boolean callEvent(ChatMessageChannel channel, String message,
                             Target target, Player... players){
        ChatMessageChannelEvent event = new ChatMessageChannelEvent(channel, message, target, players);
        BukkitLibrary.callEvent(event);

        return !event.isCancelled();
    }

    public enum Target {

        CONSOLE,
        PLAYER

    }

}
