package de.superioz.library.test;

import de.superioz.library.minecraft.server.common.item.SimpleItem;
import de.superioz.library.minecraft.server.common.item.SimpleItemTool;
import de.superioz.library.minecraft.server.event.WrappedItemInteractEvent;

import java.util.function.Consumer;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class TestItemTool extends SimpleItemTool {

    public TestItemTool(SimpleItem item, Consumer<WrappedItemInteractEvent> eventConsumer){
        super(item, eventConsumer);
    }

}
