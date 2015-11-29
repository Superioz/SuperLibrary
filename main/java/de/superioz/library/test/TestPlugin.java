package de.superioz.library.test;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.command.CommandHandler;
import de.superioz.library.minecraft.server.exception.CommandRegisterException;
import org.bukkit.craftbukkit.v1_8_R3.Overridden;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestPlugin extends JavaPlugin {

    private static ProtocolManager protocolManager;

    @Overridden
    public void onLoad(){
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable(){
        SuperLibrary.initFor(this);
        SuperLibrary.initProtocol(protocolManager);

        try{
            CommandHandler.registerCommand(TestCommandClass.class);
        }catch(CommandRegisterException e){
            System.out.println(e.getReason());
        }
    }

}
