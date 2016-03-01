package de.superioz.library.minecraft.test;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.command.CommandHandler;
import de.superioz.library.minecraft.server.common.npc.FakeMob;
import de.superioz.library.minecraft.server.exception.CommandRegisterException;
import de.superioz.library.minecraft.server.util.protocol.ProtocolUtil;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestPlugin extends JavaPlugin {

    private static ProtocolManager protocolManager;

    public void onLoad(){
        if(ProtocolUtil.checkLibrary())
            protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable(){
        SuperLibrary.initFor(this);

        if(ProtocolUtil.checkLibrary())
            SuperLibrary.initProtocol(protocolManager);

        try{
            CommandHandler.registerCommand(SpawnCommand.class);
        }
        catch(CommandRegisterException e){
            System.out.println(e.getReason());
        }
    }

}
