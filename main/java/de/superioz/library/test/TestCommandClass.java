package de.superioz.library.test;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.Command;
import de.superioz.library.minecraft.server.common.command.CommandCase;
import de.superioz.library.minecraft.server.common.command.SubCommand;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.common.inventory.InventorySize;
import de.superioz.library.minecraft.server.common.inventory.SuperInventory;
import de.superioz.library.minecraft.server.common.view.SuperScoreboard;
import de.superioz.library.minecraft.server.exception.InventoryCreateException;
import de.superioz.library.minecraft.server.util.BukkitUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */

@Command(label = "test", aliases = "t", desc = "Test", min = -1, permission = "", usage = "",
        commandTarget =
                AllowedCommandSender.PLAYER_AND_CONSOLE, tabCompleter = TestTabCompleter.class)
public class TestCommandClass implements CommandCase {

    @Override
    public void execute(CommandContext context){
        context.getSender().sendMessage(ChatColor.BLUE + "Das ist der erste Test");
    }

    // =========================================================================================

    @SubCommand(label = "inv", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void inv(CommandContext context){
        Player player = (Player) context.getSender();

        try{
            SuperInventory superInventory = new SuperInventory("&cInventory", InventorySize.FIVE_ROWS)
                    .from(TestInventoryClass.class);
            player.openInventory(superInventory.build());
        }catch(InventoryCreateException e){
            System.err.println(e.getReason());
        }
    }

    @SubCommand(label = "scoreboard", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void scoreboard(CommandContext context){
        Player player = (Player) context.getSender();

        SuperScoreboard scoreboard = new SuperScoreboard("&9Scoreboard")
                .add("&3Leben: &b" + player.getHealth()).add("&3Name: &b" + player.getDisplayName())
                .blankLine().add("&3Host: &b" + player.getAddress().getHostName()).blankLine();

        scoreboard.build().show(player);
    }

    @SubCommand.Nested(parent = "scoreboard")
    @SubCommand(label = "scoreboard1", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void scoreboard1(CommandContext context){
        Player player = (Player) context.getSender();

        SuperScoreboard scoreboard = new SuperScoreboard("&4Scoreboard")
                .add("&cLeben: &d" + player.getHealth() / 2).add("&cName: &d" + player.getDisplayName())
                .blankLine().add("&cLevel: &d" + player.getLevel()).blankLine();

        scoreboard.build().show(player);
    }

    @SubCommand(label = "red", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void red(CommandContext context){
        Player player = (Player) context.getSender();

        player.sendMessage("Flash ..");
        BukkitUtil.flashRedScreen(1, player);
        new BukkitRunnable() {
            @Override
            public void run(){
                BukkitUtil.flashRedScreen(20, player);
            }
        }.runTaskLater(SuperLibrary.plugin(), 20L);
    }

    @SubCommand(label = "tab", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void tab(CommandContext context){
        Player player = (Player) context.getSender();

        player.sendMessage("Set tab & header ..");
        BukkitUtil.setTabHeaderFooter("&cHeader!!!", "&dFooter!!!", player);
    }

    @SubCommand(label = "test2", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test2(CommandContext context){
        Player player = (Player) context.getSender();

        player.sendMessage(org.bukkit.ChatColor.RED + "Set nametag ..");
        de.superioz.library.minecraft.server.common.lab.nametag.
                NametagManager.setNametag("&9[DEV] &r", " &5[OP]", false,
                Collections.singletonList(player), Arrays.asList(BukkitUtil.onlinePlayers()));
    }

    @SubCommand(label = "test3", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test3(CommandContext context){
        Player player = (Player) context.getSender();

        System.out.println("Argumente: " + Arrays.toString(context.getArguments()) + " [" + context
                .getArgumentsLength()+"]");
    }

}
