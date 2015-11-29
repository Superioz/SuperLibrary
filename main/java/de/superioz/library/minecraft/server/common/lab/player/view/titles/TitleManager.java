package de.superioz.library.minecraft.server.common.lab.player.view.titles;

import de.superioz.library.minecraft.server.util.BukkitUtil;
import de.superioz.library.minecraft.server.util.ChatUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class created on April in 2015
 */
public class TitleManager {

    private static Class packetClass = BukkitUtil.getNMSClassExact("PacketPlayOutChat");
    private static Class chatComponentClass = BukkitUtil.getNMSClassExact("IChatBaseComponent");

    /**
     * Sends a message to given players above hotbar item name
     *
     * @param text Message to send
     * @param players Receiver of message
     */
    public static void sendHotbarMessage(String text, Player... players){
        for(Player p : players)
            sendHotbarMessagePacket(text, p);
    }

    /**
     * Sends a screen title to given user. Color codes allowed
     *
     * @param title Main title
     * @param subtitle Sub title
     * @param fadeIn Time to fade in
     * @param stay Time to stay
     * @param fadeOut Time to fade out
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean timeUnitSeconds){
        new CraftTitle(title, subtitle, new int[]{fadeIn, stay, fadeOut}).send(timeUnitSeconds, player);
    }

    public static void sendTitle(Player player, String title, String subtitle){
        sendTitle(player, title, subtitle, 1, 1, 1, true);
    }

    public static void sendTitle(Player player, String title){
        sendTitle(player, title, "");
    }


    //============================================ PROTOCOL ============================================

    @SuppressWarnings("unchecked")
    private static void sendHotbarMessagePacket(String text, Player player){
        byte target = (byte) 2;

        // Get packet
        try{
            Object chatBaseComp = BukkitUtil.getChatBaseComp("{'text':'" + ChatUtil.colored(text) + "'}");
            Object packet;

            if(packetClass != null && chatBaseComp != null){
                Constructor constructor = packetClass.getDeclaredConstructor(chatComponentClass, byte.class);
                packet = constructor.newInstance(chatBaseComp, target);

                BukkitUtil.sendPacket(packet, player);
            }
        }catch(NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e){
            e.printStackTrace();
        }

    }

}
