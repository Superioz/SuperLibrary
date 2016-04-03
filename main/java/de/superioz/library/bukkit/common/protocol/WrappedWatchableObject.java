package de.superioz.library.bukkit.common.protocol;

import lombok.Getter;

/**
 * Created on 01.04.2016.
 */
@Getter
public class WrappedWatchableObject extends AbstractWrapper {

	private WrappedDataWatcherObject dataWatcherObject;
	private Object value;

	/**
	 * Constructor for watchable object of datawatcher
	 *
	 * @param object The object
	 * @param value  The value
	 */
	public WrappedWatchableObject(WrappedDataWatcherObject object, Object value){
		this.dataWatcherObject = object;
		this.value = value;

		super.initialise(ProtocolUtil.DATA_WATCHER_ITEM.getSimpleName(), new Class<?>[]{ProtocolUtil.DATA_WATCHER_OBJECT, Object.class},
				dataWatcherObject.getHandle(), value);
	}

	/**
	 * Constructor for handle takeover
	 *
	 * @param handle The handle
	 */
	public WrappedWatchableObject(Object handle){
		this.handle = handle;
	}

}
