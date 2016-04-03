package de.superioz.library.bukkit.common.protocol;

import de.superioz.library.java.util.ReflectionUtils;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created on 30.03.2016.
 */
@Getter
public abstract class AbstractWrapper {

	/**
	 * For classes who wraps in a nms class
	 */

	protected Class<?> handleClass = null;
	protected Object handle = null;

	/**
	 * Initialises this wrapper class
	 *
	 * @param className The class name
	 * @param arguments The arguments
	 */
	protected void initialise(String className, Class<?>[] parameterTypes, Object... arguments){
		this.handleClass = ProtocolUtil.getNMSClassExact(className);

		try{
			Constructor<?> constructor;

			// Get constructor
			assert this.handleClass != null;
			if(parameterTypes != null){
				constructor = this.handleClass.getConstructor(parameterTypes);
			}
			else{
				constructor = this.handleClass.getConstructor();
			}

			// Get instance
			if(arguments.length != 0){
				handle = constructor.newInstance(arguments);
			}
			else{
				this.handle = ReflectionUtils.instantiateObject(getHandleClass(), arguments);
			}
		}
		catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
			e.printStackTrace();
			throw new RuntimeException("Couldn't init AbstractWrapper. [" + e.getMessage() + "]");
		}
	}

	protected void initialise(String className, Object... arguments){
		this.initialise(className, ReflectionUtils.getClasses(arguments), arguments);
	}

	protected void inherit(Object handle, Class<?> clazz){
		this.handle = handle;
		this.handleClass = clazz;
	}

}
