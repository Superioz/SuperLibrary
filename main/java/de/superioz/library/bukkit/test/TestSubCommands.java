package de.superioz.library.bukkit.test;

import de.superioz.library.bukkit.common.command.Command;
import de.superioz.library.bukkit.common.command.context.CommandContext;
import net.md_5.bungee.api.ChatColor;

/**
 * Created on 12.03.2016.
 */
public class TestSubCommands {

	@Command.Nested(parent = "subcommand")
	@Command(label = "subcommand4")
	public void subCommandMethod4(CommandContext context){
		context.getSenderAsPlayer().sendMessage(ChatColor.DARK_BLUE + "Sub command #4");
	}

	@Command.Nested(parent = "subcommand")
	@Command(label = "subcommand5")
	public void subCommandMethod5(CommandContext context){
		context.getSenderAsPlayer().sendMessage(ChatColor.DARK_RED + "Sub command #5");
	}

}
