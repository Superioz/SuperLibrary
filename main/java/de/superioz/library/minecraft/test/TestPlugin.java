package de.superioz.library.minecraft.test;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.command.CommandHandler;
import de.superioz.library.minecraft.server.exception.CommandRegisterException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestPlugin extends JavaPlugin {

	@Override
	public void onEnable(){
		SuperLibrary.initProtocol();
		SuperLibrary.initFor(this);
		
		try{
			CommandHandler.registerCommand(SpawnCommand.class);
		}
		catch(CommandRegisterException e){
			System.out.println(e.getReason());
		}
	}

}
