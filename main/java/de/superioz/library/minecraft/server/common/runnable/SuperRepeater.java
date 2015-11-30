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

    public void run(Consumer<BukkitRunnable> onRepeat, Consumer<BukkitRunnable> onFinish, int delay, int repeats){
        super.counter = repeats;
        super.runnable = new BukkitRunnable() {
            @Override
            public void run(){
                if(counter == 0){
                    onFinish.accept(this);
                    this.cancel();
                }

                onRepeat.accept(this);
                counter--;
            }
        };

        super.getRunnable().runTaskTimer(SuperLibrary.plugin(), 0L, delay);
    }

}
