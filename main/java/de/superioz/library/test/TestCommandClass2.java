package de.superioz.library.test;

import de.superioz.library.minecraft.server.chat.BukkitChat;
import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.CommandHandler;
import de.superioz.library.minecraft.server.common.command.CommandWrapper;
import de.superioz.library.minecraft.server.common.command.SubCommand;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import org.bukkit.entity.Player;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestCommandClass2 {

    @SubCommand(label = "info", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void info(CommandContext context){
        Player player = (Player) context.getSender();

        for(CommandWrapper c : CommandHandler.getCommands()){
            BukkitChat.send("&8- &7Command: &c" + c.getLabel(), player);

            for(CommandWrapper sc : c.getSubCommands()){
                BukkitChat.send("&8-- &7SubCommand: &6" + sc.getLabel(), player);

                if(sc.getSubCommands() == null) continue;
                for(CommandWrapper nsc : sc.getSubCommands()){
                    BukkitChat.send("&8--- &7Nested SubCommand: &e" + nsc.getLabel(), player);
                }
            }
        }
    }

    @SubCommand.Nested(parent = "info")
    @SubCommand(label = "info1", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void info1(CommandContext context){
        Player player = (Player) context.getSender();

        BukkitChat.send("&7Has Parent? &f" + context.getCommand().hasParent(), player);
    }

}
