package de.superioz.library.minecraft.server.common.runnable;

import de.superioz.library.main.SuperLibrary;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SuperDelayer extends SuperRunnable {

    public void run(Consumer<BukkitRunnable> onFinish, int delay){
        super.runnable = new BukkitRunnable() {
            @Override
            public void run(){
                onFinish.accept(this);
            }
        };

        super.getRunnable().runTaskLater(SuperLibrary.plugin(), delay);
    }


}
