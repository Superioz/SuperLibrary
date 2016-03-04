package de.superioz.library.minecraft.test;

import de.superioz.library.java.util.list.ListUtil;
import de.superioz.library.minecraft.server.common.command.AllowedCommandSender;
import de.superioz.library.minecraft.server.common.command.Command;
import de.superioz.library.minecraft.server.common.command.CommandCase;
import de.superioz.library.minecraft.server.common.command.CommandFlag;
import de.superioz.library.minecraft.server.common.command.context.CommandContext;
import de.superioz.library.minecraft.server.common.npc.FakeMob;
import de.superioz.library.minecraft.server.common.npc.MobType;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntitySettings;
import de.superioz.library.minecraft.server.message.BukkitChat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 01.03.2016.
 */
@Command(label = "superspawn", desc = "Just a command for testing things", min = 1, permission = "test.spawn",
		usage = "<flag> <parameter>", commandTarget = AllowedCommandSender.PLAYER,
		flags = {"m", "h", "f", "n", "?"})
public class SpawnCommand implements CommandCase {

	@Override
	public void execute(CommandContext context){
		Player player = context.getSenderAsPlayer();

		// Check if context contains at least one of needed flags
		String[] neededFlags = {"m", "h", "f"};
		if(!context.hasFlag(neededFlags)){
			BukkitChat.send("&cNot enough flags set! Needed (at least): " + ListUtil.insert(neededFlags, ", "), player);
			return;
		}

		// Check if player used help flag
		if(context.hasFlag("?")){
			String message = "&6?: &e";
			List<String> sl = new ArrayList<>();

			if(context.hasFlag("m")){
				for(EntityType t : EntityType.values()){
					sl.add(t.name().toLowerCase());
				}
			}
			else if(context.hasFlag("f")){
				for(MobType t : MobType.values()){
					sl.add(t.name().toLowerCase());
				}
			}

			BukkitChat.send(message + ListUtil.insert(sl, "&7,&e"), player);
			return;
		}

		// Just some variables for the loop (getting the right flag)
		CommandFlag flag = null;
		int counter = 0;

		// Loop for getting one flag
		while(flag == null){
			if(counter > neededFlags.length)
				break;
			flag = context.getCommandFlag(neededFlags[counter]);
			counter++;
		}

		// Check state of flag
		if(flag == null){
			BukkitChat.send("&cCouldn't fetch needed flag! Please check your input!", player);
			return;
		}

		// Get name of the to-spawned
		String name = "&aDefault";
		if(context.hasFlag("n")){
			CommandFlag nameFlag = context.getCommandFlag("n");

			if(!nameFlag.isEmpty())
				name = ListUtil.insert(nameFlag.getArguments(), " ");
		}

		// Get the type
		String type = getType(flag, flag.isSimilar(neededFlags[2]));
		if(type.startsWith("#")){
			BukkitChat.send("&cThis type doesn't exist! (" + type.replace("#", "") + ")", player);
			return;
		}

		// Check type of flag and spawn given
		BukkitChat.send("&7Spawn mob/human/fake mob ..", player);
		if(flag.isSimilar(neededFlags[0])){
			this.spawnMob(name, player, type, false);
		}
		else if(flag.isSimilar(neededFlags[1])){
			BukkitChat.send("&cHumans not supported yet!", player);
		}
		else if(flag.isSimilar(neededFlags[2])){
			this.spawnMob(name, player, type, true);
		}
	}

	/**
	 * Get the type of given flag arguments
	 *
	 * @param nameFlag The flag
	 * @param fake     If the command is to spawn a fake mob
	 * @return The type-argument
	 */
	public String getType(CommandFlag nameFlag, boolean fake){
		String type = EntityType.CREEPER.name();

		if(!nameFlag.isEmpty()){
			String rawType = nameFlag.getArgument(1).toUpperCase();

			try{
				if(fake){
					if(MobType.valueOf(rawType) != null)
						type = rawType;
				}
				else{
					if(EntityType.valueOf(rawType) != null)
						type = rawType;
				}
			}
			catch(IllegalArgumentException ex){
				return "#" + rawType;
			}
		}

		return type;
	}

	/**
	 * Spawns a mob with given flag and sender arguments
	 *
	 * @param name   The name
	 * @param sender The command sender
	 * @param type   The mob type
	 */
	public void spawnMob(String name, Player sender, String type, boolean fake){
		if(fake){
			MobType mobType = MobType.valueOf(type);
			FakeMob mob = new FakeMob(mobType, sender.getLocation(), name, new EntitySettings(false, true, true));
			mob.spawn(sender);
		}
		else{
			try{
				EntityType mobType = EntityType.valueOf(type);
				sender.getWorld().spawnEntity(sender.getLocation(), mobType);
			}
			catch(IllegalArgumentException ex){
				BukkitChat.send("&cFailed!", sender);
				return;
			}
		}
		BukkitChat.send("&aSuccess!", sender);
	}

}
