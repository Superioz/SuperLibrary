package de.superioz.library.bukkit.common.protocol;

import de.superioz.library.java.util.ReflectionUtils;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on 30.03.2016.
 */
public class WrappedDataWatcher extends AbstractWrapper {

	private static Method SETTER;
	private static Method GETTER;
	private static Method REGISTER;

	private static HashSet<Integer> REGISTERED_META = new HashSet<>();

	/**
	 * Initialises this class
	 */
	{
		super.initialise(ProtocolUtil.DATA_WATCHER.getSimpleName(), new Class<?>[]{ProtocolUtil.ENTITY}, (Object) null);

		// Get the set method
		Class<?> dataWatcherObjectClass = ProtocolUtil.DATA_WATCHER_OBJECT;
		try{
			SETTER = handleClass.getMethod("set", dataWatcherObjectClass, Object.class);
			REGISTER = handleClass.getMethod("register", dataWatcherObjectClass, Object.class);
			GETTER = handleClass.getMethod("get", dataWatcherObjectClass);

			SETTER.setAccessible(true);
			GETTER.setAccessible(true);
			REGISTER.setAccessible(true);
		}
		catch(NoSuchMethodException e){
			throw new RuntimeException("Couldn't initialise datawatcher.");
		}
	}

	/**
	 * Get watchable objects from this datawatcher
	 *
	 * @return The collection of datawatcher
	 */
	public Collection<WrappedWatchableObject> getWatchableObjects(){
		Map<Integer, WrappedWatchableObject> map = getMap();
		return map.values();
	}

	/**
	 * Get the watchable object map from handle class
	 *
	 * @return The map
	 */
	public Map<Integer, WrappedWatchableObject> getMap(){
		try{
			Map<Integer, Object> map = (Map<Integer, Object>) ReflectionUtils.getField(getHandleClass(), "c").get(getHandle());
			Map<Integer, WrappedWatchableObject> newMap = new HashMap<>();

			for(Integer i : map.keySet()){
				WrappedWatchableObject object = new WrappedWatchableObject(map.get(i));
				newMap.put(i, object);
			}

			return newMap;
		}
		catch(IllegalAccessException e){
			throw new RuntimeException("Couldn't get map from data watcher.");
		}
	}

	/**
	 * Set an object into the datawatcher (metadata of the entity)
	 *
	 * @param index  The index (As WrappedDataWatcherObject - An index and a serializer)
	 * @param object The object (To set into)
	 */
	private void set(WrappedDataWatcherObject index, Object object){
		try{
			if(REGISTERED_META.contains(index.getIndex())){
				SETTER.invoke(getHandle(), index.getHandle(), object);
			}
			else{
				REGISTER.invoke(getHandle(), index.getHandle(), object);
			}
		}
		catch(IllegalAccessException | InvocationTargetException e){
			throw new RuntimeException("Couldn't set/register datawatcher object.");
		}
	}

	private void set(int index, Serializer serializer, Object object){
		this.set(new WrappedDataWatcherObject(index, serializer), object);
	}

	public void set(ProtocolEntityMeta metaData, Object object){
		this.set(metaData.getIndex(), Serializer.from(metaData.getType()), object);
	}

	/**
	 * Gets an object from datawatcher with given index
	 *
	 * @param index The index as wrappeddatawatcherobject
	 * @return The result
	 */
	private Object get(WrappedDataWatcherObject index){
		try{
			return GETTER.invoke(getHandle(), index.getHandle());
		}
		catch(IllegalAccessException | InvocationTargetException e){
			throw new RuntimeException("Couldn't set datawatcher object.");
		}
	}

	private Object get(int index, Serializer serializer){
		return this.get(new WrappedDataWatcherObject(index, serializer));
	}

	public Object get(ProtocolEntityMeta metaData){
		return this.get(new WrappedDataWatcherObject(metaData.getIndex(), Serializer.from(metaData.getType())));
	}

	/**
	 * Class for wrapping a registry
	 */
	@Getter
	public enum Serializer {

		BYTE("a"),
		INTEGER("b"),
		FLOAT("c"),
		STRING("d"),
		CHAT("e"),
		OPT_ITEM("f"),
		OPT_BLOCKDATA("g"),
		BOOLEAN("h"),
		VECTOR3F("i"),
		BLOCKPOS("j"),
		OPT_BLOCKPOS("k"),
		DIRECTION("l"),
		OPT_UUID("m");

		private Class<?> handleClass;
		private String fieldName;
		private Object handle;

		/**
		 * Constructor for registry enum
		 *
		 * @param fieldName Name of the field in registry class
		 */
		Serializer(String fieldName){
			this.fieldName = fieldName;
			this.handleClass = ProtocolUtil.DATA_WATCHER_REGISTRY;

			// Get field value
			try{
				assert handleClass != null;
				this.handle = handleClass.getDeclaredField(fieldName).get(null);
			}
			catch(IllegalAccessException | NoSuchFieldException e){
				throw new RuntimeException("Couldn't init serializer.");
			}
		}

		/**
		 * Gets the serializer for given entitymeta type
		 *
		 * @param type The type
		 * @return The serializer
		 */
		public static Serializer from(ProtocolEntityMeta.Type type){
			Serializer s = null;

			switch(type){
				case BYTE:
					s = Serializer.BYTE;
					break;
				case VARINT:
					s = Serializer.INTEGER;
					break;
				case FLOAT:
					s = Serializer.FLOAT;
					break;
				case STRING:
					s = Serializer.STRING;
					break;
				case CHAT:
					s = Serializer.CHAT;
					break;
				case SLOT:
					s = Serializer.OPT_ITEM;
					break;
				case BOOLEAN:
					s = Serializer.BOOLEAN;
					break;
				case VECTOR3F:
					s = Serializer.VECTOR3F;
					break;
				case POSITION:
					s = Serializer.BLOCKPOS;
					break;
				case OPT_POSITION:
					s = Serializer.OPT_BLOCKPOS;
					break;
				case DIRECTION:
					s = Serializer.DIRECTION;
					break;
				case OPT_UUID:
					s = Serializer.OPT_UUID;
					break;
				case BLOCKID:
					s = Serializer.OPT_BLOCKDATA;
					break;
				default:
					break;
			}
			return s;
		}

	}

}
