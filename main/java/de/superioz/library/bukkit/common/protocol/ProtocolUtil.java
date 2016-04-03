package de.superioz.library.bukkit.common.protocol;

import de.superioz.library.java.util.ReflectionUtils;
import de.superioz.library.java.util.SimpleStringUtils;
import io.netty.channel.Channel;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created on 29.03.2016.
 */
public class ProtocolUtil {

	public static final Class<?> ENTITY = getNMSClassExact("Entity");
	public static final Class<?> DATA_WATCHER = getNMSClassExact("DataWatcher");
	public static final Class<?> DATA_WATCHER_OBJECT = getNMSClassExact("DataWatcherObject");
	public static final Class<?> DATA_WATCHER_REGISTRY = getNMSClassExact("DataWatcherRegistry");
	public static final Class<?> DATA_WATCHER_ITEM = getNMSClassExact("DataWatcher$Item");
	public static final Class<?> DATA_WATCHER_SERIALIZER = getNMSClassExact("DataWatcherSerializer");
	public static final Class<?> ENTITY_PLAYER = ProtocolUtil.getNMSClassExact("EntityPlayer");
	public static final Class<?> NETWORK_MANAGER = ProtocolUtil.getNMSClassExact("NetworkManager");
	public static final Class<?> PLAYER_CONNECTION = ProtocolUtil.getNMSClassExact("PlayerConnection");
	public static final Class<?> CRAFT_ITEMSTACK = ProtocolUtil.getOBCClassExact("inventory.CraftItemStack");
	public static final Class<?> ITEMSTACK = ProtocolUtil.getNMSClassExact("ItemStack");
	public static final Class<?> ENUM_ITEM_SLOT = ProtocolUtil.getNMSClassExact("EnumItemSlot");
	public static final Class<?> PLAYER_INFO_DATA = ProtocolUtil.getNMSClassExact("PacketPlayOutPlayerInfo$PlayerInfoData");
	public static final Class<?> ENUM_GAMEMODE = ProtocolUtil.getNMSClassExact("WorldSettings$EnumGamemode");
	public static final Class<?> CHAT_BASE_COMPONENT = ProtocolUtil.getNMSClassExact("IChatBaseComponent");
	public static final Class<?> CHAT_SERIALIZER = ProtocolUtil.getNMSClassExact("IChatBaseComponent$ChatSerializer");
	public static final Class<?> PLAYER_INFO_ACTION = ProtocolUtil.getNMSClassExact("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
	public static final Class<?> ENTITY_ACTION_USE = ProtocolUtil.getNMSClassExact("PacketPlayInUseEntity$EnumEntityUseAction");
	public static final Class<?> TITLE_ACTION = ProtocolUtil.getNMSClassExact("PacketPlayOutTitle$EnumTitleAction");
	public static final Class<?> WORLD_BORDER_ACTION = ProtocolUtil.getNMSClassExact("PacketPlayOutWorldBorder$EnumWorldBorderAction");
	public static final Class<?> BOSSBAR_COLOR = ProtocolUtil.getNMSClassExact("BossBattle$BarColor");
	public static final Class<?> BOSSBAR_STYLE = ProtocolUtil.getNMSClassExact("BossBattle$BarStyle");
	public static final Class<?> BOSSBAR_ACTION = ProtocolUtil.getNMSClassExact("PacketPlayOutBoss$Action");

	/**
	 * Get given itemstack as nms object
	 *
	 * @param itemStack The itemstack
	 * @return The nmc object
	 */
	public static Object asNMSCopy(ItemStack itemStack){
		try{
			Method method = CRAFT_ITEMSTACK.getDeclaredMethod("asNMSCopy", ItemStack.class);
			return method.invoke(null, itemStack);
		}
		catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
			throw new RuntimeException("Couldn't get nms copy of itemstack.");
		}
	}

	/**
	 * Get enum gamemode from given native gamemode
	 *
	 * @param gameMode The gamemode
	 * @return The result
	 */
	public static Object getEnumGameMode(EnumWrappers.NativeGameMode gameMode){
		assert ENUM_GAMEMODE != null;
		return ENUM_GAMEMODE.getEnumConstants()[gameMode.ordinal()];
	}

	/**
	 * Get chat base component from given string
	 *
	 * @param message The message as string
	 * @return The component
	 */
	public static Object getChatBaseComponent(String message){
		Method fromString = ReflectionUtils.getMethod(CHAT_SERIALIZER, "a", String.class);

		try{
			return fromString.invoke(null, "{\"text\": \"" + message + "\"}");
		}
		catch(IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
			throw new RuntimeException("Couldn't get chat base component.");
		}
	}

	/**
	 * Returns the text from given chat base
	 *
	 * @param chatBase The chat base
	 * @return The string
	 */
	public static String getText(Object chatBase){
		Method fromChatBase = ReflectionUtils.getMethod(CHAT_SERIALIZER, "a", CHAT_BASE_COMPONENT);

		try{
			return (String) fromChatBase.invoke(null, chatBase);
		}
		catch(IllegalAccessException | InvocationTargetException e){
			throw new RuntimeException("Couldn't get text from chat base.");
		}
	}

	/**
	 * Get the networkmanager object from given player
	 *
	 * @param player The player
	 * @return The network manager
	 */
	public static Object getNetworkManager(Player player){
		try{
			Field playerConnectionField = ReflectionUtils.getField(ENTITY_PLAYER, "playerConnection");
			Field networkManagerField = ReflectionUtils.getField(PLAYER_CONNECTION, "networkManager");

			return networkManagerField.get(playerConnectionField.get(ProtocolUtil.getHandle(player)));
		}
		catch(IllegalAccessException e){
			throw new RuntimeException("Couldn't get network manager.");
		}
	}

	/**
	 * Get the channel object from given networkManager object
	 *
	 * @param networkManager The networkManager
	 * @return The channel
	 */
	public static Channel getChannel(Object networkManager){
		try{
			Field channelField = ReflectionUtils.getField(NETWORK_MANAGER, "channel");
			channelField.setAccessible(true);
			return (Channel) channelField.get(networkManager);
		}
		catch(IllegalAccessException e){
			throw new RuntimeException("Couldn't get channel.");
		}
	}

	/**
	 * Get the entity class for given type
	 *
	 * @param type  The type
	 * @param world The world
	 * @return The nms entity object
	 */
	public static Object getNMSEntity(ProtocolEntityType type, World world){
		String className = "Entity" + SimpleStringUtils.upperFirstLetterSpaced(type.name().toLowerCase(), "_");
		Class<?> entityClass = getNMSClassExact(className);
		Object craftWorld = getHandle(world);

		return ReflectionUtils.instantiateObject(entityClass, craftWorld);
	}

	/**
	 * Get version of current bukkit
	 *
	 * @return The version as string
	 */
	public static String getVersion(){
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	/**
	 * Send given protocol to given player
	 *
	 * @param packet The protocol
	 * @param player The player
	 */
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
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Send protocol to given players
	 *
	 * @param packet  The protocol
	 * @param players The players
	 */
	public static void sendPacket(Object packet, Player... players){
		for(Player p : players){
			sendPacket(packet, p);
		}
	}

	/**
	 * Send given protocol to given players except given other players
	 *
	 * @param packet  The protocol
	 * @param except  The exceptions
	 * @param players The players
	 */
	public static void sendPacket(Object packet, List<Player> except, Player... players){
		for(Player p : players){
			if(except.contains(p)) continue;
			sendPacket(packet, p);
		}
	}

	/**
	 * Get handle of given world
	 *
	 * @param world The world
	 * @return The handle
	 */
	public static Object getHandle(World world){
		Object nms_world = null;
		Method entity_getHandle = ReflectionUtils.getMethod(world.getClass(), "getHandle");
		try{
			nms_world = entity_getHandle != null ? entity_getHandle.invoke(world) : null;
		}
		catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
		}
		return nms_world;
	}

	/**
	 * Get handle of given protocol
	 *
	 * @param entity The protocol
	 * @return The handle
	 */
	public static Object getHandle(Entity entity){
		Object obj = null;
		try{
			obj = entity.getClass().getMethod("getHandle").invoke(entity);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * Get the package of the nms
	 *
	 * @return The name
	 */
	public static String getNMSPackage(){
		return "net.minecraft.server." + getVersion();
	}

	/**
	 * Get the package of the obc
	 *
	 * @return The name
	 */
	public static String getOBCPackage(){
		return "org.bukkit.craftbukkit." + getVersion();
	}

	/**
	 * Gets nms class with given name
	 *
	 * @param exactName The name
	 * @return The class
	 */
	public static Class<?> getNMSClassExact(String exactName){
		Class<?> clazz;
		try{
			clazz = Class.forName(getNMSPackage() + "." + exactName);
		}
		catch(Exception e){
			return null;
		}

		return clazz;
	}

	/**
	 * Gets obc class with given name
	 *
	 * @param exactName The name
	 * @return The class
	 */
	public static Class<?> getOBCClassExact(String exactName){
		Class<?> clazz;
		try{
			clazz = Class.forName(getOBCPackage() + "." + exactName);
		}
		catch(Exception e){
			return null;
		}

		return clazz;
	}

	/**
	 * Get nms class object
	 */
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

	/**
	 * Encodes given parameter to a simple position
	 *
	 * @param x The x-coord
	 * @param y The y-coord
	 * @param z The z-coord
	 * @return The encoded position
	 */
	public static int encodePosition(int x, int y, int z){
		return ((x & 0x3FFFFFF) << 38) | ((y & 0xFFF) << 26) | (z & 0x3FFFFFF);
	}

	/**
	 * Decodes the position
	 *
	 * @param val The encoded position
	 * @return The int array
	 */
	public static int[] decodePosition(int val){
		int x = val >> 38;
		int y = (val >> 26) & 0xFFF;
		int z = val << 38 >> 38;
		return new int[]{x, y, z};
	}

	/**
	 * Convert a boolean to a byte
	 *
	 * @param b The boolean
	 * @return The byte
	 */
	public static byte convertToByte(boolean b){
		return (byte) (b ? 0x01 : 0x00);
	}

	/**
	 * Convert a boolean to an integer
	 *
	 * @param b The boolean
	 * @return The integer
	 */
	public static int convertToInteger(boolean b){
		return (b ? 1 : 0);
	}

	/**
	 * Returns the decimal color from given chatcolor
	 *
	 * @param c The chat color
	 * @return The decimal color
	 */
	public static int getDecimalColor(ChatColor c){
		return Integer.parseInt(c.getChar() + "", 16);
	}

}
