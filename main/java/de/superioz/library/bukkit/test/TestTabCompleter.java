package de.superioz.library.bukkit.test;

import de.superioz.library.bukkit.common.command.BukkitTabCompleter;
import de.superioz.library.bukkit.common.command.CommandHandler;
import de.superioz.library.bukkit.common.command.CommandWrapper;
import de.superioz.library.bukkit.common.command.context.TabCompleterContext;

import java.util.Arrays;
import java.util.List;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class TestTabCompleter extends BukkitTabCompleter {

    public TestTabCompleter(CommandWrapper wrapper){
        super(wrapper);
    }

    @Override
    public List<String> onTabComplete(TabCompleterContext context){
        CommandHandler.getCommands();
        return Arrays.asList("one", "two", "three");
    }

}
