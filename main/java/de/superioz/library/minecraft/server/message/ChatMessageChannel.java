package de.superioz.library.minecraft.server.message;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.event.ChatMessageChannelEvent;
import de.superioz.library.minecraft.server.util.ChatUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperLibrary
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

    public String spacePrefix(boolean flag){
        return flag ? (prefix+" ") : prefix;
    }

    protected String getMessage(String msg, boolean spacePrefix){
        return !spacePrefix ? ChatUtil.colored(prefix + msg) : ChatUtil.colored(prefix + " " + msg);
    }

    protected boolean callEvent(ChatMessageChannel channel, String message,
                             Target target, Player... players){
        ChatMessageChannelEvent event = new ChatMessageChannelEvent(channel, message, target, players);
        SuperLibrary.callEvent(event);

        return !event.isCancelled();
    }

    public enum Target {

        CONSOLE,
        PLAYER

    }

}
