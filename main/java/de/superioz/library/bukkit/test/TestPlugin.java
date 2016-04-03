package de.superioz.library.bukkit.test;

import de.superioz.library.bukkit.BukkitLibrary;
import de.superioz.library.bukkit.common.command.CommandHandler;
import de.superioz.library.bukkit.common.protocol.*;
import de.superioz.library.bukkit.exception.CommandRegisterException;
import net.minecraft.server.v1_9_R1.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable(){
		BukkitLibrary.initFor(this);

		try{
			CommandHandler.registerCommand(TestCommand.class, TestSubCommands.class);
		}
		catch(CommandRegisterException e){
			e.printStackTrace();
		}
	}

}
