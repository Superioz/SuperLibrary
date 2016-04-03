package de.superioz.library.bukkit.util;

import org.bukkit.ChatColor;

/**
 * Class created on April in 2015
 */
public class ChatUtil {

    /**
     * Colors given string
     *
     * @param s The string
     *
     * @return The colored string
     */
    public static String colored(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Colors all given strings
     *
     * @param strings The strings
     *
     * @return The colored strings
     */
    public static String[] colored(String... strings){
        String[] colored = new String[strings.length];

        for(int i = 0; i < colored.length; i++){
            colored[i] = colored(strings[i]);
        }

        return colored;
    }

}
