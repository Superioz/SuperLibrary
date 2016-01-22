package de.superioz.library.minecraft.server.lab.fakemob.defined;

import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntityAppearence;
import de.superioz.library.minecraft.server.lab.fakemob.data.FakeHumanProfile;
import de.superioz.library.minecraft.server.lab.fakemob.data.settings.FakeEntitySettings;
import de.superioz.library.minecraft.server.lab.fakemob.root.CraftFakeHuman;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class FakeHuman extends CraftFakeHuman {

    protected FakeHuman(FakeEntityAppearence appearence, FakeEntitySettings settings, FakeHumanProfile humanProfile){
        super(appearence, settings, humanProfile);
    }

    protected FakeHuman(FakeEntityAppearence appearence, boolean showName, FakeHumanProfile humanProfile){
        this(appearence, new FakeEntitySettings(false, false, showName), humanProfile);
    }

    public FakeHuman(String name, Location loc, boolean showName, FakeHumanProfile humanProfile){
        this(new FakeEntityAppearence(name, loc, EntityType.PLAYER),
                new FakeEntitySettings(false, false, showName), humanProfile);
    }

    public FakeHuman(String name, Location loc, FakeHumanProfile humanProfile){
        this(new FakeEntityAppearence(name, loc, EntityType.PLAYER),
                new FakeEntitySettings(false, false, true), humanProfile);
    }

    public FakeHuman(String name, Location loc, String skinName, String capeUrl){
        this(new FakeEntityAppearence(name, loc, EntityType.PLAYER),
                new FakeEntitySettings(false, false, true), new FakeHumanProfile(skinName, capeUrl));
    }

    public FakeHuman(String name, Location loc, String skinOwner){
        this(new FakeEntityAppearence(name, loc, EntityType.PLAYER),
                new FakeEntitySettings(false, false, true), new FakeHumanProfile("", skinOwner));
    }

    public FakeHuman(String name, Location loc){
        this(new FakeEntityAppearence(name, loc, EntityType.PLAYER),
                new FakeEntitySettings(false, false, true), new FakeHumanProfile("", name));
    }

    public FakeHuman(Location loc){
        this(new FakeEntityAppearence("Human", loc, EntityType.PLAYER),
                new FakeEntitySettings(false, false, true), new FakeHumanProfile("", ""));
    }

}
