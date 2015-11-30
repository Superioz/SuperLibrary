package de.superioz.library.minecraft.server.common.runnable;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
@Getter
public abstract class SuperRunnable {

    @Setter
    protected int counter = 0;
    protected BukkitRunnable runnable;

}
