package de.superioz.library.minecraft.server.common.runnable;

import de.superioz.library.main.SuperLibrary;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
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
    public void run(Consumer<BukkitRunnable> onRepeat, Consumer<BukkitRunnable> onFinish, int delay){
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

        super.getRunnable().runTaskTimer(SuperLibrary.plugin(), 0L, delay);
    }

}
