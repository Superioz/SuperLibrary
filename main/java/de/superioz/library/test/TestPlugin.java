package de.superioz.library.test;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.command.CommandHandler;
import de.superioz.library.minecraft.server.common.npc.FakeMob;
import de.superioz.library.minecraft.server.exception.CommandRegisterException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestPlugin extends JavaPlugin {

    private static ProtocolManager protocolManager;
    public static FakeMob testMob;

    public void onLoad(){
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable(){
        SuperLibrary.initFor(this);
        SuperLibrary.initProtocol(protocolManager);

        try{
            CommandHandler.registerCommand(TestCommandClass.class, TestCommandClass2.class);
        }catch(CommandRegisterException e){
            System.out.println(e.getReason());
        }
    }

}
