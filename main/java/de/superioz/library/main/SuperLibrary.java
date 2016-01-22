package de.superioz.library.main;

import com.comphenix.protocol.ProtocolManager;
import de.superioz.library.minecraft.server.listener.DefaultCommandListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SuperLibrary {
    private static ProtocolManager protocolManager;
    private static JavaPlugin plugin;
    private static PluginManager pluginManager;
    private static ExecutorService executorService;

    public static void initProtocol(ProtocolManager manager){
        protocolManager = manager;
    }

    public static void initFor(JavaPlugin pl){
        plugin = pl;
        pluginManager = Bukkit.getServer().getPluginManager();
        executorService = Executors.newCachedThreadPool();

        // register default listener
        registerListener();
    }

    // -- Intern methods

    public static JavaPlugin plugin(){
        return plugin;
    }

    public static PluginManager pluginManager(){
        return pluginManager;
    }

    public static ProtocolManager protocolManager(){
        return protocolManager;
    }

    public static void callEvent(Event event){
        pluginManager().callEvent(event);
    }

    private static void registerListener(){
        pluginManager().registerEvents(new DefaultCommandListener(), plugin());
    }

    public static ExecutorService getExecutorService(){
        return executorService;
    }

    public static void registerListener(Listener listener){
        pluginManager().registerEvents(listener, plugin());
    }

}
