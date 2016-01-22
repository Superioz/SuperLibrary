package de.superioz.library.minecraft.server.message;

import de.superioz.library.minecraft.server.util.BukkitUtil;
import de.superioz.library.minecraft.server.util.ChatUtil;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitChat {

    /**
     * Only important for me tho
     */
    public static void send(String message, Player... players){
        for(Player p : players)
            p.sendMessage(ChatUtil.colored(message));
    }

    /**
     * Only important for me tho
     */
    public static void broadcast(String message){
        send(message, BukkitUtil.onlinePlayers());
    }

}
