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
@Setter
public abstract class SuperRunnable {

    public SuperRunnable(int counter){
        setCounter(counter);
    }

    protected int counter;
    protected BukkitRunnable runnable;

}
