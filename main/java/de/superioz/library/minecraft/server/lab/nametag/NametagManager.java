package de.superioz.library.minecraft.server.lab.nametag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.util.BukkitUtilities;
import de.superioz.library.minecraft.server.util.ChatUtil;
import de.superioz.library.minecraft.server.util.ProtocolUtil;
import de.superioz.library.minecraft.server.util.protocol.WrapperPlayServerScoreboardTeam;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class NametagManager {

    private static final String TEAM_PREFIX = "SL";
    private static final HashMap<TeamHandle, List<String>> TEAMS = new HashMap<>();
    private static final List<Integer> IDENTIFIER = new ArrayList<>();

    public static void setNametag(String prefix, String suffix, boolean tab,
                                  List<Player> target, List<Player> viewer){
        PacketContainer packet;
        HashMap<Player, String> tabNames = new HashMap<>();
        for(Player p : target) tabNames.put(p, p.getPlayerListName());

        if(getTeam(prefix, suffix) == null){
            createTeam(prefix, suffix, target);
            packet = getPacket(getTeam(prefix, suffix),
                    WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        }
        else{
            TeamHandle team = getTeam(prefix, suffix);
            packet = getPacket(team, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED);
        }

        TeamHandle team = getTeam(prefix, suffix);
        assert team != null;

        for(Player p : viewer)
            ProtocolUtil.sendServerPacket(packet, p);

        if(!tab){
            for(Player p : tabNames.keySet()){
                p.setPlayerListName(ChatColor.RESET + p.getName());
            }
        }
    }

    public static void updateNametag(List<Player> target, List<Player> viewer, boolean tab){
        for(Player player : target){
            TeamHandle team = getTeam(player);

            if(team == null)
                continue;

            setNametag(team.getPrefix(), team.getSuffix(), tab,
                    Collections.singletonList(player), viewer);
        }
    }

    public static void clearNametag(Player player, List<Player> viewer){
        TeamHandle team = getTeam(player);

        if(team == null)
            return;

        List<String> l = TEAMS.get(team);
        l.remove(player.getDisplayName());

        PacketContainer packet = getPacket(team,
                WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        ProtocolUtil.sendServerPacket(packet, viewer);

        if(TEAMS.get(team).size() == 0){
            deleteTeam(team);
        }
    }

    /// ===============================================================================================

    private static PacketContainer getPacket(TeamHandle team, int mode){
        PacketContainer packetc = SuperLibrary.protocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam(packetc);

        if(!TEAMS.containsKey(team))
            return packet.getHandle();

        packet.setPrefix(ChatUtil.colored(team.getPrefix()));
        packet.setSuffix(ChatUtil.colored(team.getSuffix()));
        packet.setMode(mode);
        packet.setName(team.getName());
        packet.setDisplayName(team.getName());
        packet.setPlayers(TEAMS.get(team));
        System.out.println("Packet: '"+packet.getPrefix()+"','"+packet.getSuffix()+"','"+packet.getDisplayName()+"'" +
                ",'"+packet.getPlayers().toString()+"'");

        return packet.getHandle();
    }

    private static String getNextTeamName(){
        int index = 0;

        while(IDENTIFIER.contains(index)) {
            index++;
        }

        IDENTIFIER.add(index);
        return TEAM_PREFIX + index;
    }

    private static TeamHandle getTeam(int id){
        for(TeamHandle th : TEAMS.keySet()){
            if(th.getName().equalsIgnoreCase(TEAM_PREFIX+id))
                return th;
        }
        return null;
    }

    private static TeamHandle getTeam(String prefix, String suffix){
        for(TeamHandle th : TEAMS.keySet()){
            if(th.getPrefix().equalsIgnoreCase(prefix) &&
                    th.getSuffix().equalsIgnoreCase(suffix))
                return th;
        }
        return null;
    }

    private static TeamHandle getTeam(Player player){
        for(TeamHandle th : TEAMS.keySet()){
            if(TEAMS.get(th).contains(player.getDisplayName()))
                return th;
        }
        return null;
    }

    private static void createTeam(String prefix, String suffix, List<Player> players){
        List<String> ls = players.stream().map(Player::getDisplayName)
                .collect(Collectors.toList());

        TeamHandle team = new TeamHandle(getNextTeamName(), prefix, suffix);
        TEAMS.put(team, ls);
    }

    private static void deleteTeam(TeamHandle team){
        if(TEAMS.get(team).size()!=0)
            return;

        PacketContainer packet = getPacket(team,
                WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED);
        ProtocolUtil.sendServerPacket(packet, BukkitUtilities.onlinePlayers());

        TEAMS.remove(team);
    }

    @Getter
    @Setter
    public static class TeamHandle {

        private String name;
        private String prefix;
        private String suffix;

        public TeamHandle(String name, String prefix, String suffix){
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
        }

    }

}
