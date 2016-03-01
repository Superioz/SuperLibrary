package de.superioz.library.java.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class created on April in 2015
 */
public class ReflectionUtils {

    /**
     * Gets a declared field from the class
     * The parameter is 'clazz' because 'class' is already
     * used.
     */
    public static Field getField(Class<?> clazz, String name) {
        Object obj = null;
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            obj = f;
        } catch (NoSuchFieldException ex) {
            //
        }
        return (Field) obj;
    }

    /**
     * Gets a method out of this class
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        try {
            for (Method m : clazz.getMethods()) {
                if (m.getName().equals(name) && classListEquals(args, m.getParameterTypes())) {
                    return m;
                }
            }
        } catch (Exception e) {
            //
        }
        return clazz.getMethods()[0];
    }

    /**
     * Returns a method, that has the same name +
     * the same parameterLength
     */
    public static Method getMethodWith(Class<?> clazz, String name, int parameterLength) {
        Object obj = null;
        try {
            for (Method m : clazz.getMethods()) {
                if (m.getName().equals(name) && m.getParameterTypes().length == parameterLength) {
                    obj = m;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return (Method) obj;
    }

    /**
     * Set a field.
     */
    public static void setField(String field, Object obj, Object instance) {
        Field f;
        try {
            f = instance.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(instance, obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simply returns a class object
     */
    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get constructor of given class with given parameter
     *
     * @param clazz          The class
     * @param parameterTypes The parameter
     * @return The constructor
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        for (Constructor<?> con : clazz.getConstructors()) {
            if (classListEquals(con.getParameterTypes(), parameterTypes)) {
                return con;
            }
        }
        return clazz.getConstructors()[0];
    }

    /**
     * Instantiate given class with given objects
     *
     * @param clazz     The class
     * @param arguments The objects
     * @return The inits object
     */
    public static Object instantiateObject(Class<?> clazz, Object... arguments) {
        try {
            return getConstructor(clazz, getClasses(arguments)).newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object instantiateObject(String className, Object... arguments){
        return instantiateObject(getClass(className), arguments);
    }

    /**
     * Get the class of given types
     *
     * @param arguments The arguments
     * @return The classes array
     */
    public static Class<?>[] getClasses(Object... arguments) {
        Class<?>[] classes = new Class[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            classes[i] = arguments[i].getClass();
        }
        return classes;
    }

    /**
     * Invoke an object into a method
     *
     * @param instance   The object instance
     * @param methodName The method name
     * @param arguments  The arguments
     * @return The returned object
     */
    public static Object invokeMethod(Object instance, String methodName, Object... arguments) {
        try {
            return getMethod(instance.getClass(), methodName).invoke(instance, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * Checks if first class list contains other
     */
    public static boolean classListEquals(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;

        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }

        return equal;
    }

}
