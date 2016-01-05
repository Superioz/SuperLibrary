package de.superioz.library.minecraft.server.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import de.superioz.library.java.util.ReflectionUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class BukkitUtil {

    public static boolean verifyUsername(String name){
        return (name != null)
                && !(name.isEmpty())
                && !(name.length() > 16);
    }

    public static Entity getEntity(World world, int entityID){
        for(Entity e : world.getEntities()){
            if(e.getEntityId() == entityID)
                return e;
        }
        return null;
    }

    public static Player[] onlinePlayers(){
        Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
        return pl.toArray(new Player[pl.size()]);
    }

    public static void pushAwayEntity(Entity e, Location from, double speed){
        Vector unit = e.getLocation().toVector().subtract(from.toVector()).normalize();
        e.setVelocity(unit.multiply(speed));
    }

    public static boolean compareInventory(Inventory first, Inventory second){
        if(first == null || second == null) return true;
        if(!first.getTitle().equals(second.getTitle())) return false;
        if(first.getType() != second.getType()) return false;
        ItemStack[] firstContents = first.getContents();
        ItemStack[] secondContents = second.getContents();
        if(firstContents.length != secondContents.length) return false;
        for(int i = 0; i < firstContents.length; i++){
            if(firstContents[i] == null || secondContents[i] == null){
                continue;
            }
            else if(!firstContents[i].isSimilar(secondContents[i]))
                return false;
        }
        return true;
    }

    public static boolean hasContent(PlayerInventory inventory){
        for(ItemStack item : inventory.getContents()){
            if(item != null
                    && item.getType() != Material.AIR)
                return true;
        }
        for(ItemStack item : inventory.getArmorContents()){
            if(item != null
                    && item.getType() != Material.AIR)
                return true;
        }
        return false;
    }

    public static String getVersion(){
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public static void sendPacket(Object packet, Player player){
        try{
            Object craftPlayer = player.getClass()
                    .getMethod("getHandle").invoke(player);
            Object playerConn = craftPlayer.getClass()
                    .getField("playerConnection").get(craftPlayer);

            Method m = ReflectionUtils.getMethodWith(playerConn.getClass(), "sendPacket", 1);

            if(m != null){
                m.invoke(playerConn, packet);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void sendPacket(Object packet, Player... players){
        for(Player p : players){ sendPacket(packet, p); }
    }

    public static void sendPacket(Object packet, List<Player> except, Player... players){
        for(Player p : players){
            if(except.contains(p)) continue;
            sendPacket(packet, p);
        }
    }

    public static Object getHandle(World world){
        Object nms_world = null;
        Method entity_getHandle = ReflectionUtils.getMethod(world.getClass(), "getHandle");
        try{
            nms_world = entity_getHandle != null ? entity_getHandle.invoke(world) : null;
        }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return nms_world;
    }

    public static Object getHandle(Entity entity){
        Object obj = null;
        try{
            obj = entity.getClass().getMethod("getHandle").invoke(entity);
        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    public static String getNMSPackage(){
        return "net.minecraft.server." + getVersion();
    }

    public static String getOBCPackage(){
        return "org.bukkit.craftbukkit." + getVersion();
    }

    public static Class<?> getNMSClassExact(String exactName){
        Class<?> clazz;
        try{
            clazz = Class.forName(getNMSPackage() + "." + exactName);
        }catch(Exception e){
            return null;
        }

        return clazz;
    }

    public static Class<?> getOBCClassExact(String exactName){
        Class<?> clazz;
        try{
            clazz = Class.forName(getOBCPackage() + "." + exactName);
        }catch(Exception e){
            return null;
        }

        return clazz;
    }

    public static Object getNMSClassObject(String exactName, Object... args) throws Exception{
        Class<?> clazz = getNMSClassExact(exactName);
        Object obj = null;

        int parameter = 0;
        if(args != null){
            parameter = args.length;
        }
        for(Constructor<?> con : (clazz != null ? clazz.getConstructors() : new Constructor<?>[0])){
            if(con.getParameterTypes().length == parameter){
                obj = con.newInstance(args);
            }
        }
        return obj;
    }

    public static Object getGamemodeHandle(GameMode gameMode){
        Class c = getNMSClassExact("EnumGamemode");
        if(c == null){
            c = getNMSClassExact("WorldSettings$EnumGamemode");
        }
        try{
            Method method = ReflectionUtils.getMethod(c, "getById", new Class<?>[]{int.class});
            assert method != null;
            method.setAccessible(true);
            return method.invoke(null, gameMode.getValue());
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Object getNMSTextComp(String text){
        if(text == null || text.isEmpty()){
            return null;
        }
        Class c = getNMSClassExact("ChatComponentText");
        try{
            assert c != null;
            Constructor constructor = c.getDeclaredConstructor(String.class);
            return constructor.newInstance(text);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Object getChatBaseComp(String text){
        Class chatBase = getNMSClassExact("IChatBaseComponent$ChatSerializer");

        Method m;
        try{
            if(chatBase != null){
                m = chatBase.getDeclaredMethod("a", String.class);
                return m.invoke(chatBase, text);
            }
            return null;
        }catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }

        return null;
    }

    public static Object getSessionService(){
        Server server = Bukkit.getServer();
        try{
            Object mcServer = server.getClass().getDeclaredMethod("getServer").invoke(server);
            for(Method m : mcServer.getClass().getMethods()){
                if(m.getReturnType().getSimpleName().equalsIgnoreCase("MinecraftSessionService")){
                    return m.invoke(mcServer);
                }
            }
        }catch(Exception ex){
            throw new IllegalStateException("An error occurred while trying to get the session service", ex);
        }
        throw new IllegalStateException("No session service found.");
    }

    public static Method getFillMethod(Object sessionService){
        for(Method m : sessionService.getClass().getDeclaredMethods()){
            if(m.getName().equals("fillProfileProperties")){
                return m;
            }
        }
        throw new IllegalStateException("No fillProfileProperties method found in the session service.");
    }

    public static Collection<WrappedSignedProperty> getTextureProperties(String name){
        WrappedGameProfile profile = WrappedGameProfile
                .fromOfflinePlayer(Bukkit.getOfflinePlayer(name));
        Object handle = profile.getHandle();
        Object sessionService = getSessionService();
        try{
            Method method = getFillMethod(sessionService);
            method.invoke(sessionService, handle, true);
        }catch(IllegalAccessException | InvocationTargetException ex){
            ex.printStackTrace();
        }
        profile = WrappedGameProfile.fromHandle(handle);
        return profile.getProperties().get("textures");
    }

    public static void setTabHeaderFooter(String header, String footer, Player... players){
        sendTabHeaderFooter(header, footer, players);
    }

    public static void setTabName(Player player, String newName){
        player.setPlayerListName(ChatUtil.colored(newName));
    }

    private static void sendTabHeaderFooter(String header, String footer, Player... players){
        PacketContainer packet = de.superioz.library.main.SuperLibrary.protocolManager()
                .createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        // Text
        if(header == null)
            header = "Header";
        if(footer == null)
            footer = "Footer";

        // Set components
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(ChatUtil.colored(header)));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(ChatUtil.colored(footer)));

        // Send packet
        ProtocolUtil.sendServerPacket(packet, players);
    }

    public static void flashRedScreen(int redness, Player player){
        // Redness
        int value = redness;
        if(redness < 0 || redness > 20){
            value = 1;
        }
        int dist = -50000 * value + 1000000;

        // Packet
        PacketContainer packet = initWorldBorder(dist, player);

        // Send packet
        ProtocolUtil.sendServerPacket(packet, player);
    }

    private static PacketContainer getPacket(){
        return de.superioz.library.main.SuperLibrary
                .protocolManager().createPacket(PacketType.Play.Server.WORLD_BORDER);
    }

    private static PacketContainer initWorldBorder(int distance, Player player){
        PacketContainer packet = getPacket();
        packet.getWorldBorderActions().write(0, EnumWrappers.WorldBorderAction.INITIALIZE);
        packet.getIntegers()
                .write(0, 29999984)
                .write(1, 15)
                .write(2, distance);
        packet.getLongs()
                .write(0, 0L);
        packet.getDoubles()
                .write(0, player.getLocation().getX())
                .write(1, player.getLocation().getZ())
                .write(2, 200000.0D)
                .write(3, 200000.0D);

        return packet;
    }

}
