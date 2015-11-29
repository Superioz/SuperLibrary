package de.superioz.library.minecraft.server.common.lab.fakemob.data.settings;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class FakeEntityAppearence {

    protected String name;
    protected Location loc;
    protected EntityType type;

    public FakeEntityAppearence(String name, Location loc, EntityType type){
        this.name = name;
        this.loc = loc;
        this.type = type;
    }

    public Location getLoc(){
        return loc;
    }

    public void setLoc(Location loc){
        this.loc = loc;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public EntityType getType(){
        return type;
    }

    public void setType(EntityType type){
        this.type = type;
    }
}
