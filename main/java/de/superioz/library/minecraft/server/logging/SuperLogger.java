package de.superioz.library.minecraft.server.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Class created on April in 2015
 */
public class SuperLogger {

    protected Plugin plugin;
    protected LogCache logCache;
    protected String consolePrefix;
    protected PluginDescriptionFile desc;
    protected ConsoleLogColor consoleLogColor = ConsoleLogColor.NONE;

    public SuperLogger(Plugin plugin){
        this.plugin = plugin;
        this.logCache = new LogCache(plugin, "logs");
        this.desc = plugin.getDescription();
        this.consolePrefix = "[" + desc.getName() + "] ";
    }

    /**
     * Only important for me tho
     */
    public void log(LogTarget target, ConsoleLogColor color, String message){
        switch(target){
            case CONSOLE:
                Bukkit.getConsoleSender().sendMessage(color.getColor() + this.consolePrefix + message);
                break;
            case LOG_FILE:
                logCache.log(message);
                break;
            case CONSOLE_AND_FILE:
                Bukkit.getConsoleSender().sendMessage(color.getColor() + message);
                logCache.log(message);
                break;
            default: break;
        }
    }

    /**
     * Only important for me tho
     */
    public void consoleLog(String message){
        this.log(LogTarget.CONSOLE, consoleLogColor, message);
    }

    /**
     * Only important for me tho
     */
    public void fileLog(String message){
        this.log(LogTarget.LOG_FILE, ConsoleLogColor.NONE, message);
    }

    /**
     * Only important for me tho
     */
    public void completeLog(String message){
        this.log(LogTarget.CONSOLE_AND_FILE, consoleLogColor, message);
    }

    /**
     * Only important for me tho
     */
    public void completeLog(ConsoleLogColor color, String message){
        this.log(LogTarget.CONSOLE_AND_FILE, color, message);
    }

    /**
     * Only important for me tho
     */
    public void consoleLog(ConsoleLogColor color, String message){
        this.log(LogTarget.CONSOLE, color, message);
    }

    /**
     * Only important for me tho
     */
    public void setColor(ConsoleLogColor color){
        this.consoleLogColor = color;
    }

    /**
     * Only important for me tho
     */
    public LogCache cache(){
        return logCache;
    }

    public enum LogTarget {
        CONSOLE,
        LOG_FILE,
        CONSOLE_AND_FILE;
    }

    public enum ConsoleLogColor {

        MAGENTA(ChatColor.LIGHT_PURPLE),
        RED(ChatColor.RED),
        YELLOW(ChatColor.YELLOW),
        GREEN(ChatColor.GREEN),
        BLUE(ChatColor.BLUE),
        WHITE(ChatColor.WHITE),
        NONE(ChatColor.RESET);

        private ChatColor color;

        ConsoleLogColor(ChatColor color){
            this.color = color;
        }

        public ChatColor getColor(){
            return color;
        }
    }

}
