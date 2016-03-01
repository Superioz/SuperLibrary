package de.superioz.library.minecraft.server.common.runnable;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public abstract class SuperRunnable {

    public SuperRunnable(int counter){
        setCounter(counter);
    }

    protected int counter;
    protected BukkitRunnable runnable;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }
}
