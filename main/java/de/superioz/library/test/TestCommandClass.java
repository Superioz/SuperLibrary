package de.superioz.library.test;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.Command;
import de.superioz.library.minecraft.server.common.command.CommandCase;
import de.superioz.library.minecraft.server.common.command.SubCommand;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.common.inventory.InventorySize;
import de.superioz.library.minecraft.server.common.inventory.PageableInventory;
import de.superioz.library.minecraft.server.common.inventory.SuperInventory;
import de.superioz.library.minecraft.server.common.item.SimpleItem;
import de.superioz.library.minecraft.server.common.runnable.SuperRepeater;
import de.superioz.library.minecraft.server.common.view.SuperScoreboard;
import de.superioz.library.minecraft.server.event.WrappedInventoryClickEvent;
import de.superioz.library.minecraft.server.exception.InventoryCreateException;
import de.superioz.library.minecraft.server.util.BukkitUtil;
import de.superioz.library.minecraft.server.util.GeometryUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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
        de.superioz.library.minecraft.server.lab.nametag.
                NametagManager.setNametag("&9[DEV] &r", " &5[OP]", false,
                Collections.singletonList(player), Arrays.asList(BukkitUtil.onlinePlayers()));
    }

    @SubCommand(label = "test3", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test3(CommandContext context){
        Player player = (Player) context.getSender();

        SuperRepeater repeater = new SuperRepeater(30);
        repeater.run(bukkitRunnable -> {
            int counter = repeater.getCounter();

            if(counter % 10 == 0){
                player.sendMessage(org.bukkit.ChatColor.BLUE + "Kann durch 10 geteilt werden! > " + counter);
            }
            else if(counter <= 10){
                player.sendMessage(org.bukkit.ChatColor.GOLD + "Die letzten Sekunden! > " + counter);
            }
        }, bukkitRunnable -> player.sendMessage(org.bukkit.ChatColor.RED + "> 0 > Fertig! Ende Gelände!"), 10);
    }

    @SubCommand(label = "test4", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test4(CommandContext context){
        Player player = (Player) context.getSender();

        TestItemTool itemTool = new TestItemTool(new SimpleItem(Material.REDSTONE_COMPARATOR, 1).setName
                ("&cComperator"), event -> {
            if(event.getAction() != Action.LEFT_CLICK_BLOCK){
                return;
            }

            Block b = event.getClickedBlock();
            Set<Block> blocks = GeometryUtil.fill4(b.getWorld(), b.getX(), b.getY(), b.getZ(),
                    Material.STAINED_CLAY, true);
            for(Block bl : blocks){ bl.setType(Material.STAINED_GLASS); }

            event.getEvent().setCancelled(true);
        });

        player.sendMessage(ChatColor.YELLOW + "Set tool in hand ...");
        player.getInventory().setItemInHand(itemTool.getItem().getWrappedStack());
    }

    @SubCommand(label = "test5", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test5(CommandContext context){
        Player player = (Player) context.getSender();

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta3 = (SkullMeta) skull.getItemMeta();
        meta3.setOwner(player.getName());
        meta3.setDisplayName("§6Freunde");
        skull.setItemMeta(meta3);

        player.getInventory().setItem(8, skull);
        player.sendMessage(">> Test5");
    }

    @SubCommand(label = "test6", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test6(CommandContext context){
        Player player = (Player) context.getSender();

        List<SimpleItem> items = new ArrayList<>();
        Material[] materials = new Material[]{
                Material.STONE, Material.GRASS, Material.DIRT, Material.WOOL, Material.BEACON, Material.BEDROCK, Material.BED,
                Material.SNOW_BLOCK, Material.WORKBENCH, Material.ANVIL
        };
        for(Material mat : materials){
            for(int i = 0; i < 9; i++){
                items.add(new SimpleItem(mat).setName("&8------ "));
            }
        }

        PageableInventory inventory = new PageableInventory("Title", InventorySize.FOUR_ROWS, items, new SimpleItem(Material.STAINED_GLASS),
                new SimpleItem(Material.ARROW), new SimpleItem(Material.WOOD_DOOR));
        inventory.calculatePages(false, event -> {
            event.getPlayer().sendMessage(ChatColor.RED + "" + event.getItem().getType());
            event.cancelEvent();
        });
        player.openInventory(inventory.getPage(1).build());

        player.sendMessage(">> Test6");
    }

    @SubCommand(label = "test7", desc = "Das ist eine Beschreibung", permission = "test", usage = "",
            commandTarget = AllowedCommandSender.PLAYER)
    public void test7(CommandContext context){
        Player player = (Player) context.getSender();

        List<SimpleItem> items = new ArrayList<>();
        Material[] materials = new Material[]{
                Material.STONE, Material.GRASS, Material.DIRT, Material.WOOL, Material.BEACON, Material.BEDROCK, Material.BED,
                Material.SNOW_BLOCK, Material.WORKBENCH, Material.ANVIL
        };
        for(Material mat : materials){
            for(int i = 0; i < 9; i++){
                items.add(new SimpleItem(mat).setName(mat.name() + " &b#" + 0));
            }
        }

        PageableInventory inventory = new PageableInventory("Title", InventorySize.FOUR_ROWS, items, new SimpleItem(Material.STAINED_GLASS),
                new SimpleItem(Material.ARROW), new SimpleItem(Material.WOOD_DOOR));
        inventory.calculatePages(false, WrappedInventoryClickEvent::cancelEvent);
        inventory.open(player);

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run(){
                if(counter > 1000 || inventory.getViewers().size() == 0){
                    player.sendMessage(ChatColor.RED + "Runnable cancelled!");
                    this.cancel();
                    return;
                }

                List<SimpleItem> newItems = new ArrayList<>();
                for(Material mat : materials){
                    for(int i = 0; i < 9; i++){
                        newItems.add(new SimpleItem(mat).setName(mat.name() + " &b#" + counter));
                    }
                }

                inventory.setObjects(newItems);
                inventory.update();
                player.sendMessage(ChatColor.BLUE + "Counter: " + counter);
                counter++;
            }
        }.runTaskTimer(SuperLibrary.plugin(), 0L, 20L);
    }

}
