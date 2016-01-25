package de.superioz.library.minecraft.server.common.npc.raw.holo;

import de.superioz.library.main.SuperLibrary;
import de.superioz.library.minecraft.server.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public abstract class CraftHologram {

    protected static final double DISTANCE = 0.25;
    protected List<String> lines;
    protected List<CraftHologramPart> parts;
    private boolean active;
    private Location location;

    public CraftHologram(Location loc, String... lines){
        this.lines = Arrays.asList(lines);
        this.parts = new ArrayList<>();
        this.active = false;
        this.location = LocationUtil.fix(loc.getBlock().getLocation());
    }

    /**
     * Add a line to the existing hologram
     *
     * @param text The text
     */
    public void add(String text){
        this.lines.add(text);
    }

    /**
     * Resets the hologram
     *
     * @param text Also every line?
     */
    public void reset(boolean text){
        this.parts.clear();

        if(text)
            this.lines.clear();
    }

    /**
     * Resets the text
     *
     * @param newLines The new lines for the hologram
     */
    public void resetText(String... newLines){
        this.lines = Arrays.asList(newLines);
    }

    /**
     * Convert the hologram
     */
    public void convert(){
        Location firstLine = this.location.clone().add(0, (this.lines.size() / 2) * DISTANCE, 0);

        for(String s : lines){
            this.parts.add(new CraftHologramPart(firstLine.clone(), s));
            firstLine = firstLine.subtract(0, DISTANCE, 0);
        }
    }

    /**
     * Shows this hologram for given players
     *
     * @param players The viewers
     */
    public void show(Player... players){
        if(!isActive()){
            this.convert();

            for(CraftHologramPart holo : this.getParts()){
                holo.spawn(players);
            }

            this.active = true;
        }
    }

    /**
     * Hide this hologram for given players
     *
     * @param players The viewers
     */
    public void hide(Player... players){
        if(isActive()){
            for(CraftHologramPart holo : this.getParts()){
                holo.despawn(players);
            }

            this.active = false;
            this.reset(false);
        }
    }

    /**
     * Updates the hologram for given players
     *
     * @param players The viewers
     */
    public void update(final Player... players){
        if(isActive()){
            this.hide(players);

            Bukkit.getScheduler().runTaskLater(SuperLibrary.plugin(), () -> show(players), 1L);
        }
        else{
            this.show(players);
        }
    }

    // -- Intern methods

    public List<String> getLines(){
        return lines;
    }

    public String getLine(int index){
        return this.getPart(index).getText();
    }

    public void setLines(List<String> lines){
        this.lines = lines;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public List<CraftHologramPart> getParts(){
        return parts;
    }

    public CraftHologramPart getPart(int index){
        return this.parts.get(index);
    }

    public void setParts(List<CraftHologramPart> parts){
        this.parts = parts;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

}
