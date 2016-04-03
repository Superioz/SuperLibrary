package de.superioz.library.bukkit.test;

import de.superioz.library.bukkit.common.Hologram;
import de.superioz.library.bukkit.common.ViewManager;
import de.superioz.library.bukkit.common.command.AllowedCommandSender;
import de.superioz.library.bukkit.common.command.Command;
import de.superioz.library.bukkit.common.command.CommandCase;
import de.superioz.library.bukkit.common.command.context.CommandContext;
import de.superioz.library.bukkit.common.npc.NPCHuman;
import de.superioz.library.bukkit.common.npc.NPCMob;
import de.superioz.library.bukkit.common.protocol.*;
import de.superioz.library.bukkit.common.protocol.WrappedDataWatcher;
import de.superioz.library.bukkit.util.ChatUtil;
import de.superioz.library.bukkit.util.LocationUtil;
import net.minecraft.server.v1_9_R1.PacketPlayOutBoss;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created on 01.03.2016.
 */
@Command(label = "testcommand", desc = "Just a command for testing things", permission = "test.command",
		usage = "<flag> <parameter>", commandTarget = AllowedCommandSender.PLAYER,
		flags = {"?"})
public class TestCommand implements CommandCase {

	@Override
	public void execute(CommandContext context){
		Player sender = context.getSenderAsPlayer();
		Location loc = sender.getLocation();

		ViewManager.sendHotbarMessage("&7Spawn...", sender);

		// Code here
		Hologram holo = new Hologram(loc, "&cEin Hologram!", "&bZweite Zeile :)", "&aDritte Zeile!");
		holo.show(sender);

		ViewManager.sendBossbar("&bDas ist eine Bossbar!", UUID.randomUUID(), EnumWrappers.BossbarColor.BLUE,
				EnumWrappers.BossbarStyle.NOTCHED_20, 1F,
				EnumWrappers.BossbarAction.ADD, sender);
	}

}
