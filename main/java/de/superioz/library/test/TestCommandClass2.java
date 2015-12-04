package de.superioz.library.test;

import de.superioz.library.minecraft.server.message.BukkitChat;
import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.CommandHandler;
import de.superioz.library.minecraft.server.common.command.CommandWrapper;
import de.superioz.library.minecraft.server.common.command.SubCommand;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.message.MessageChannel;
import de.superioz.library.minecraft.server.message.PlayerMessager;
import org.bukkit.entity.Player;

import java.util.Arrays;

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

        System.out.println("Argumente: " + Arrays.toString(context.getArguments()));
        for(int i = 1; i < context.getArgumentsLength()+1; i++){
            System.out.println("#" + i + " > " + context.getArgument(i)); }
    }

    @SubCommand.Nested(parent = "info")
    @SubCommand(label = "info1", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void info1(CommandContext context){
        Player player = (Player) context.getSender();
        PlayerMessager messager = new PlayerMessager("&6TEST &8|");

        messager.write("&eHallo. Das ist ein Test", true, MessageChannel.CHAT, player);
        messager.write("&eHallo. Das ist ein Test", true, MessageChannel.ACTIONBAR, player);
        messager.write("&eHallo. Das ist ein Test", true, MessageChannel.TITLE, player);

        System.out.println("Argumente: " + Arrays.toString(context.getArguments()));

        if(context.getArgumentsLength() > 1){
            String a = context.getArgument(1);
            System.out.println("Argument #1: " + a);
        }

        for(int i = 1; i < context.getArgumentsLength()+1; i++){
            System.out.println("#" + i + " > " + context.getArgument(i)); }
    }
}
