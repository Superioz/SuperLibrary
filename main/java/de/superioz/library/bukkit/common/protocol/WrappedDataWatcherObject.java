package de.superioz.library.bukkit.common.protocol;

import lombok.Getter;

/**
 * Class for wrapping a datawatcher object
 */
@Getter
public class WrappedDataWatcherObject extends AbstractWrapper {

	private int index;
	private WrappedDataWatcher.Serializer serializer;

	public WrappedDataWatcherObject(int index, WrappedDataWatcher.Serializer serializer){
		this.index = index;
		this.serializer = serializer;
		super.initialise(ProtocolUtil.DATA_WATCHER_OBJECT.getSimpleName(),
				new Class<?>[]{int.class, ProtocolUtil.DATA_WATCHER_SERIALIZER},
				index, serializer.getHandle());
	}

}
