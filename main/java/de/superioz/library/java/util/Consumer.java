package de.superioz.library.java.util;

/**
 * Created on 05.03.2016.
 */
public abstract class Consumer<T> {

	/**
	 * I like Java 8 but if not here's a class for a consumer
	 * A custom method to handle with given object
	 *
	 * @param t The object
	 */
	public abstract void accept(T t);

}
