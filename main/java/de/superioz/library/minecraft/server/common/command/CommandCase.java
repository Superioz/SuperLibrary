package de.superioz.library.minecraft.server.common.command;

import de.superioz.library.minecraft.server.common.command.context.CommandContext;

/**
 * This class was created as a part of SuperLibrary
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
