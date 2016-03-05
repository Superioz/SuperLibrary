package de.superioz.library.minecraft.server.common.runnable;

import de.superioz.library.java.util.Consumer;
import de.superioz.library.main.SuperLibrary;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SuperDelayer extends SuperRunnable {

    public SuperDelayer(int counter){
        super(counter);
    }

    /**
     * Run method
     * @param onFinish What happens after finishing?
     */
    public void run(final Consumer<BukkitRunnable> onFinish){
        super.runnable = new BukkitRunnable() {
            @Override
            public void run(){
                onFinish.accept(this);
            }
        };

        super.getRunnable().runTaskLater(SuperLibrary.plugin(), getCounter());
    }


}
