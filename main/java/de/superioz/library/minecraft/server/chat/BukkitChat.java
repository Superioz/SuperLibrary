package de.superioz.library.minecraft.server.chat;

import de.superioz.library.minecraft.server.util.BukkitUtil;
import de.superioz.library.minecraft.server.util.ChatUtil;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitChat {

    public static void send(String message, Player... players){
        for(Player p : players)
            p.sendMessage(ChatUtil.colored(message));
    }

    public static void broadcast(String message){
        send(message, BukkitUtil.onlinePlayers());
    }

}
