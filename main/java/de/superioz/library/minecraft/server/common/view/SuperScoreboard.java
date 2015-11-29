package de.superioz.library.minecraft.server.common.view;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import de.superioz.library.minecraft.server.util.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
@Getter
public class SuperScoreboard {

    @Setter
    protected String header;

    protected Scoreboard scoreboard;
    protected Map<String, Integer> lines;
    private List<Team> teams;

    public SuperScoreboard(String header, Scoreboard scoreboard){
        this.header = header;
        this.scoreboard = scoreboard;
        this.lines = new LinkedHashMap<>();
        this.teams = new ArrayList<>();
    }

    public SuperScoreboard(String header){
        this(header, Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public SuperScoreboard blankLine() {
        return add(" ");
    }

    public SuperScoreboard add(String text) {
        return add(text, null);
    }

    public SuperScoreboard add(String text, Integer score) {
        text = ChatUtil.colored(text);
        Preconditions.checkArgument(text.length() < 48,
                "Text length is larger than 48 chars");

        text = fixDuplicates(text);
        this.lines.put(text, score);
        return this;
    }

    private String fixDuplicates(String text) {
        while (this.lines.containsKey(text))
            text += ChatColor.RESET;
        if (text.length() > 48)
            text = text.substring(0, 47);
        return text;
    }

    private Map.Entry<Team, String> createTeam(String text) {
        String result;
        if (text.length() <= 16)
            return new AbstractMap.SimpleEntry<>(null, text);
        Team team = scoreboard.registerNewTeam("text-" + scoreboard.getTeams().size());
        Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
        team.setPrefix(iterator.next());
        result = iterator.next();
        if (text.length() > 32)
            team.setSuffix(iterator.next());
        teams.add(team);
        return new AbstractMap.SimpleEntry<>(team, result);
    }

    public SuperScoreboard build() {
        Objective obj = scoreboard.registerNewObjective((getHeader().length() > 16
                ? getHeader().substring(0, 15) : getHeader()), "dummy");
        obj.setDisplayName(ChatUtil.colored(getHeader()));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int index = getLines().size();

        for (Map.Entry<String, Integer> text : getLines().entrySet()) {
            Map.Entry<Team, String> team = createTeam(text.getKey());
            Integer score = text.getValue() != null ? text.getValue() : index;
            String entry = team.getValue();
            if (team.getKey() != null)
                team.getKey().addEntry(entry);
            obj.getScore(entry).setScore(score);
            index -= 1;
        }
        return this;
    }

    public void show(Player... players){
        for(Player p : players)
            p.setScoreboard(getScoreboard());
    }

    public SuperScoreboard reset(boolean header) {
        if(header)
            this.setHeader(null);

        this.lines.clear();
        this.teams.forEach(Team::unregister);
        this.teams.clear();
        return this;
    }

}
