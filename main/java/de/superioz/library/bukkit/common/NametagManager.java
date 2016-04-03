package de.superioz.library.bukkit.common;

import de.superioz.library.bukkit.common.protocol.PacketType;
import de.superioz.library.bukkit.common.protocol.WrappedPacket;
import de.superioz.library.bukkit.util.BukkitUtilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class NametagManager {

	private static final String TEAM_PREFIX = "SPRL";
	private static final HashMap<TeamHandle, List<String>> TEAMS = new HashMap<>();
	private static final List<Integer> IDENTIFIER = new ArrayList<>();
	private static final int SCOREBOARD_TEAM_CREATED = 0;
	private static final int SCOREBOARD_TEAM_REMOVED = 1;
	private static final int SCOREBOARD_TEAM_UPDATED = 2;
	private static final int SCOREBOARD_PLAYERS_ADDED = 3;
	private static final int SCOREBOARD_PLAYERS_REMOVED = 4;

	/**
	 * Set the nametag for given players with given viewer
	 *
	 * @param resetTab Should the tab get resetted
	 * @param prefix   The prefix
	 * @param suffix   The suffix
	 * @param target   The target
	 * @param viewer   The viewer
	 */
	public static void setNametag(boolean resetTab, String prefix, String suffix, List<Player> target, Player... viewer){
		WrappedPacket packet;
		HashMap<Player, String> tabNames = new HashMap<>();
		for(Player p : target){
			tabNames.put(p, p.getPlayerListName());
		}

		if(getTeam(prefix, suffix) == null){
			createTeam(prefix, suffix, target);
			packet = getScoreboardPacket(getTeam(prefix, suffix),
					SCOREBOARD_TEAM_CREATED, TEAMS);
		}
		else{
			TeamHandle team = getTeam(prefix, suffix);
			packet = getScoreboardPacket(team, SCOREBOARD_PLAYERS_ADDED, TEAMS);
		}

		TeamHandle team = getTeam(prefix, suffix);
		assert team != null;

		// --
		packet.send(viewer);

		if(resetTab){
			for(Player p : tabNames.keySet()){
				p.setPlayerListName(ChatColor.RESET + p.getName());
			}
		}
	}

	/**
	 * Updates the nameTag for given players
	 *
	 * @param target   The target players
	 * @param resetTab Should the tab get resetted
	 * @param viewer   The viewer
	 */
	public static void updateNametag(List<Player> target, boolean resetTab, Player... viewer){
		for(Player player : target){
			TeamHandle team = getTeam(player);

			if(team == null)
				continue;

			setNametag(resetTab, team.getPrefix(), team.getSuffix(),
					Collections.singletonList(player), viewer);
		}
	}

	/**
	 * Clears the nameTag for given player
	 *
	 * @param player The player
	 * @param viewer The viewer
	 */
	public static void clearNametag(Player player, Player... viewer){
		TeamHandle team = getTeam(player);

		if(team == null)
			return;

		List<String> l = TEAMS.get(team);
		l.remove(player.getDisplayName());

		WrappedPacket packet = getScoreboardPacket(team, SCOREBOARD_PLAYERS_REMOVED, TEAMS);
		packet.send(viewer);

		if(TEAMS.get(team).size() == 0){
			deleteTeam(team);
		}
	}

	/**
	 * Get the name for the next team
	 *
	 * @return The team name
	 */
	private static String getNextTeamName(){
		int index = 0;

		while(IDENTIFIER.contains(index)){
			index++;
		}

		IDENTIFIER.add(index);
		return TEAM_PREFIX + index;
	}

	/**
	 * Get team with given id
	 *
	 * @param id The id
	 * @return The team
	 */
	private static TeamHandle getTeam(int id){
		for(TeamHandle th : TEAMS.keySet()){
			if(th.getName().equalsIgnoreCase(TEAM_PREFIX + id))
				return th;
		}
		return null;
	}

	/**
	 * Get the team with given prefix & suffix
	 *
	 * @param prefix The prefix
	 * @param suffix The suffix
	 * @return The team
	 */
	private static TeamHandle getTeam(String prefix, String suffix){
		for(TeamHandle th : TEAMS.keySet()){
			if(th.getPrefix().equalsIgnoreCase(prefix) &&
					th.getSuffix().equalsIgnoreCase(suffix))
				return th;
		}
		return null;
	}

	/**
	 * Get the team of given player
	 *
	 * @param player The player
	 * @return The team
	 */
	private static TeamHandle getTeam(Player player){
		for(TeamHandle th : TEAMS.keySet()){
			if(TEAMS.get(th).contains(player.getDisplayName()))
				return th;
		}
		return null;
	}

	/**
	 * Creates a team with given preferences
	 *
	 * @param prefix  The prefix
	 * @param suffix  The suffix
	 * @param players The players
	 */
	private static void createTeam(String prefix, String suffix, List<Player> players){
		List<String> ls = new ArrayList<>();
		for(Player p : players){
			ls.add(p.getDisplayName());
		}

		TeamHandle team = new TeamHandle(getNextTeamName(), prefix, suffix);
		TEAMS.put(team, ls);
	}

	/**
	 * Deletes given team
	 *
	 * @param team The team
	 */
	private static void deleteTeam(TeamHandle team){
		if(TEAMS.get(team).size() != 0)
			return;

		WrappedPacket packet = getScoreboardPacket(team, SCOREBOARD_TEAM_REMOVED, TEAMS);
		packet.send(BukkitUtilities.onlinePlayers());

		TEAMS.remove(team);
	}

	/**
	 * Get the scoreboard packet
	 *
	 * @param team  For team
	 * @param mode  For mode
	 * @param teams For teams
	 * @return The packet
	 */
	private static WrappedPacket getScoreboardPacket(NametagManager.TeamHandle team, int mode,
	                                                 HashMap<NametagManager.TeamHandle, List<String>> teams){
		WrappedPacket packetc = new WrappedPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

		if(!teams.containsKey(team)){
			return packetc;
		}

		packetc.getStrings().write(0, team.getName());
		packetc.getStrings().write(1, team.getName());
		packetc.getStrings().write(2, team.getPrefix());
		packetc.getStrings().write(3, team.getSuffix());
		packetc.get(Collection.class).write(0, teams.get(team));
		packetc.getIntegers().write(1, mode);

		return packetc;
	}

	public static class TeamHandle {

		private String name;
		private String prefix;
		private String suffix;

		public TeamHandle(String name, String prefix, String suffix){
			this.name = name;
			this.prefix = prefix;
			this.suffix = suffix;
		}

		public String getName(){
			return name;
		}

		public String getPrefix(){
			return prefix;
		}

		public String getSuffix(){
			return suffix;
		}
	}

}
