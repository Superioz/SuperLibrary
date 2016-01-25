package de.superioz.library.minecraft.server.common.npc;

import de.superioz.library.minecraft.server.common.npc.meta.FakeHumanProfile;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntityAppearence;
import de.superioz.library.minecraft.server.common.npc.meta.settings.EntitySettings;
import de.superioz.library.minecraft.server.common.npc.raw.CraftFakeHuman;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class FakeHuman extends CraftFakeHuman {

    public FakeHuman(EntityAppearence appearence, EntitySettings settings, FakeHumanProfile humanProfile){
        super(appearence, settings, humanProfile);
    }

}
