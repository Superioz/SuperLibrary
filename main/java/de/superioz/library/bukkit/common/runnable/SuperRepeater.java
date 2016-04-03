package de.superioz.library.bukkit.common.runnable;

import de.superioz.library.java.util.Consumer;
import de.superioz.library.bukkit.BukkitLibrary;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class was created as a part of BukkitLibrary
 *
 * @author Superioz
 */
public class SuperRepeater extends SuperRunnable {

    public SuperRepeater(int counter){
        super(counter);
    }

    /**
     * Run method
     * @param onRepeat What happens on repeat?
     * @param onFinish What happens on finish?
     * @param delay Delay between
     */
    public void run(final Consumer<BukkitRunnable> onRepeat, final Consumer<BukkitRunnable> onFinish, int delay){
        super.setRunnable(new BukkitRunnable() {
            @Override
            public void run(){
                if(getCounter() == 0){
                    onFinish.accept(this);
                    this.cancel();
                    return;
                }

                onRepeat.accept(this);
                setCounter(getCounter()-1);
            }
        });

        super.getRunnable().runTaskTimer(BukkitLibrary.plugin(), 0L, delay);
    }

}
