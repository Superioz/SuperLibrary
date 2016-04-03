package de.superioz.library.bukkit;

import de.superioz.library.bukkit.common.command.CommandHandler;
import de.superioz.library.bukkit.common.npc.NPCHandler;
import de.superioz.library.bukkit.common.protocol.PacketHandler;
import de.superioz.library.bukkit.common.protocol.PacketInjector;
import de.superioz.library.bukkit.exception.CommandRegisterException;
import de.superioz.library.bukkit.listener.DefaultCommandListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class BukkitLibrary {

	private static JavaPlugin plugin;
	private static PluginManager pluginManager;
	private static ExecutorService executorService;

	/**
	 * Initialises the library with given bukkit javaplugin instance
	 *
	 * @param pl The bukkit plugin instance
	 */
	public static void initFor(JavaPlugin pl){
		plugin = pl;
		pluginManager = Bukkit.getServer().getPluginManager();
		executorService = Executors.newCachedThreadPool();

		// Register default listener
		registerListener();

		// Register the npc
		NPCHandler.init();
	}

	/**
	 * Registers a command
	 *
	 * @param commandClass      The class
	 * @param subCommandClasses The subcommand classes (if exist)
	 * @throws CommandRegisterException
	 */
	public static void registerCommand(Class<?> commandClass, Class<?>... subCommandClasses) throws CommandRegisterException{
		CommandHandler.registerCommand(commandClass, subCommandClasses);
	}

	/**
	 * Adds given packet handler to packet listener
	 * @param handler The packet handler
	 */
	public static void addPacketHandler(PacketHandler handler){
		PacketInjector.addPacketHandler(handler);
	}

	/**
	 * Removes given packet handler
	 * @param handler The packet handler
	 */
	public static void removePacketHandler(PacketHandler handler){
		PacketInjector.removePacketHandler(handler);
	}


	// -- Intern methods

	public static JavaPlugin plugin(){
		return plugin;
	}

	public static PluginManager pluginManager(){
		return pluginManager;
	}

	public static void callEvent(Event event){
		pluginManager().callEvent(event);
	}

	private static void registerListener(){
		pluginManager().registerEvents(new DefaultCommandListener(), plugin());
		pluginManager().registerEvents(new PacketInjector(), plugin());
	}

	public static ExecutorService getExecutorService(){
		return executorService;
	}

	public static void registerListener(Listener listener){
		pluginManager().registerEvents(listener, plugin());
	}

}
