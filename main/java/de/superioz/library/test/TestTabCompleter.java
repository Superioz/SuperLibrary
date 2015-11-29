package de.superioz.library.test;

import de.superioz.library.minecraft.server.command.BukkitTabCompleter;
import de.superioz.library.minecraft.server.command.CommandWrapper;
import de.superioz.library.minecraft.server.command.context.TabCompleterContext;

import java.util.Arrays;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestTabCompleter extends BukkitTabCompleter {

    public TestTabCompleter(CommandWrapper wrapper){
        super(wrapper);
    }

    @Override
    public List<String> onTabComplete(TabCompleterContext context){
        return Arrays.asList("one", "two", "three");
    }

}
