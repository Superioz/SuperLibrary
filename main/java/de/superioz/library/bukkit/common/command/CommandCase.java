package de.superioz.library.bukkit.common.command;

import de.superioz.library.bukkit.common.command.context.CommandContext;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public interface CommandCase {

    /**
     * Executes the command
     *
     * @param context The context
     */
    void execute(CommandContext context);

}
