package de.superioz.library.bukkit.message;

import de.superioz.library.bukkit.util.BukkitUtilities;
import de.superioz.library.bukkit.util.ChatUtil;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of BukkitLibrary
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
        send(message, BukkitUtilities.onlinePlayers());
    }

}
