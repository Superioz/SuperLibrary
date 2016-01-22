package de.superioz.library.minecraft.server.lab.fakemob.defined;

import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntityAppearence;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntitySettings;
import de.superioz.library.minecraft.server.lab.fakemob.root.CraftFakeMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class FakeMob extends CraftFakeMob {

    protected FakeMob(FakeEntityAppearence appearence, FakeEntitySettings settings){
        super(appearence, settings);
    }

    protected FakeMob(EntityType type, Location loc, String name, FakeEntitySettings settings){
        super(new FakeEntityAppearence(name, loc, type), settings);
    }

    public FakeMob(EntityType type, Location loc, String name, boolean showName){
        this(type, loc, name, new FakeEntitySettings(false, false, showName));
    }

    public FakeMob(EntityType type, Location loc, String name){
        this(type, loc, name, true);
    }

    public FakeMob(EntityType type, Location loc){
        this(type, loc, type.name());
    }

    public FakeMob(Location loc){
        this(EntityType.CREEPER, loc);
    }

}
