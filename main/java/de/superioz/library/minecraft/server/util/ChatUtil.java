package de.superioz.library.minecraft.server.util;

import org.bukkit.ChatColor;

/**
 * Class created on April in 2015
 */
public class ChatUtil {

    public static String colored(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[] colored(String... strings){
        String[] colored = new String[strings.length];

        for(int i = 0; i < colored.length; i++){
            colored[i] = colored(strings[i]);
        }

        return colored;
    }

}
