package de.superioz.library.java.util;

import java.lang.reflect.Field;
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
    public static Field getField(Class<?> clazz, String name){
        Object obj = null;
        try{
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            obj = f;
        }catch(NoSuchFieldException ex){

        }
        return (Field) obj;
    }

    /**
     * Gets a method out of this class
     */
    public static Method getMethod(Class<?> clazz, String name){
        Object obj = null;
        try{
            for(Method m : clazz.getMethods()){
                if(m.getName().equals(name)){
                    obj = m;
                }
            }
        }catch(Exception e){
            return null;
        }
        return (Method) obj;
    }

    /**
     * Gets a method out of this class
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>[] args){
        Object obj = null;
        try{
            for(Method m : clazz.getMethods()){
                if (m.getName().equals(name) && classListEquals(args, m.getParameterTypes())) {
                    obj = m;
                }
            }
        }catch(Exception e){
            return null;
        }
        return (Method) obj;
    }

    /**
     * Returns a method, that has the same name +
     * the same parameterLength
     */
    public static Method getMethodWith(Class<?> clazz, String name, int parameterLength){
        Object obj = null;
        try{
            for(Method m : clazz.getMethods()){
                if(m.getName().equals(name) && m.getParameterTypes().length == parameterLength){
                    obj = m;
                }
            }
        }catch(Exception e){
            return null;
        }

        return (Method) obj;
    }

    /**
     * Set a field.
     */
    public static void setField(String field, Object obj, Object instance){
        Field f;
        try{
            f = instance.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(instance, true);
        }catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    /**
     * Simply returns a class object
     */
    public static Class<?> getClass(String name){
        try{
            return Class.forName(name);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
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
