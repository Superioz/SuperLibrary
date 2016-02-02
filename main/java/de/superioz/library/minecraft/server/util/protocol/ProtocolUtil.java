package de.superioz.library.minecraft.server.util.protocol;

import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Class created on April in 2015
 */
public class ProtocolUtil {

    /**
     * Sends a packetcontainer to given players
     *
     * @param pc the packetcontainer
     * @param p  The players
     */
    public static void sendServerPacket(PacketContainer pc, Player... p){
        try{
            if(p.length == 0){
                for(final Player pl : Bukkit.getOnlinePlayers()){
                    SuperLibrary.protocolManager().sendServerPacket(pl, pc);
                }
            }
            else{
                for(Player pl : p){
                    SuperLibrary.protocolManager().sendServerPacket(pl, pc);
                }
            }
        }catch(InvocationTargetException e){
            e.printStackTrace();
        }
    }

    /**
     * Checks if ProtocolLib is enabled
     *
     * @return The result
     */
    public static boolean checkLibrary(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        return plugin != null && plugin.isEnabled();
    }

    /**
     * Not important. Util method
     */
    public static void sendServerPacket(PacketContainer pc, List<Player> not, Player... p){
        try{
            if(p.length == 0){
                for(final Player pl : Bukkit.getOnlinePlayers()){
                    if(not.contains(pl)) continue;
                    SuperLibrary.protocolManager().sendServerPacket(pl, pc);
                }
            }
            else{
                for(Player pl : p){
                    if(not.contains(pl)) continue;
                    SuperLibrary.protocolManager().sendServerPacket(pl, pc);
                }
            }
        }catch(InvocationTargetException e){
            e.printStackTrace();
        }
    }

    /**
     * Turns value to byte
     */
    public static byte toByte(boolean flag){
        return (byte) (flag ? 1 : 0);
    }

    public static byte toByte(boolean flag, int bitMask){
        return (byte) (flag ? bitMask : 0);
    }

    public static boolean fromByte(byte b){
        return b == 1;
    }

    /**
     * Turns value to byte
     */
    public static byte toByte(int integer){
        return (byte) integer;
    }

    /**
     * Turns value to decimal color
     */
    public static int getDecimalColor(ChatColor c){
        return Integer.parseInt(c.getChar() + "", 16);
    }

}
