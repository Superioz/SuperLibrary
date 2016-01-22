package de.superioz.library.java.util.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil {

    /**
     * Returns object which is most in the list
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMostlyRepeated(List<?> list){
        Object object = null;
        Map<Object, Integer> map = new HashMap<>();
        for(Object obj : list){
            if(!map.containsKey(obj)){
                map.put(obj, 1);
            }
            else{
                int i = map.get(obj);
                map.put(obj, ++i);
            }
        }

        int highestRepeat = 0;
        for(Object obj : map.keySet()){
            int i = map.get(obj);
            if(i > highestRepeat){
                highestRepeat = i;
            }
        }

        for(Object obj : map.keySet()){
            int i = map.get(obj);
            if(i == highestRepeat){
                object = obj;
            }
        }
        return (T) object;
    }

    /**
     * Get object with highest integer value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getHighstObject(Map<?, Integer> hashmap){
        Object object = null;
        int highest = 0;

        for(Object obj : hashmap.keySet()){
            int i = hashmap.get(obj);
            if(i > highest){
                highest = i;
            }
        }

        for(Object obj : hashmap.keySet()){
            int i = hashmap.get(obj);
            if(i == highest){
                object = obj;
            }
        }
        return (T) object;
    }

    /**
     * Checks if the array contains the object
     */
    public static boolean listContains(Object[] list, Object obj){
        boolean b = false;
        for(Object ob : list){
            if(ob.equals(obj))
                b = true;
        }

        return b;
    }

    /**
     * @see #listContains(Object[], Object)
     */
    public static boolean listContainsWithArgs(Object[] list, Object obj, int amount){
        boolean b = false;
        if(list.length == amount &&
                listContains(list, obj)){
            b = true;
        }

        return b;
    }

    /**
     * Insert given string into string array
     *
     * @param array The array
     * @param str   The pattern
     *
     * @return The finished string
     */
    public static String insert(String[] array, String str){
        String s = "";
        for(int i = 0; i < array.length; i++){
            if(i == array.length - 1)
                s = s + array[i];
            else
                s = s + array[i] + str;
        }

        return s;
    }

    /**
     * Insert given string into list
     *
     * @param list The list
     * @param str  The pattern
     *
     * @return The finished string
     */
    public static String insert(List<String> list, String str){
        String s = "";
        for(int i = 0; i < list.size(); i++){
            if(i == list.size() - 1)
                s = s + list.get(i);
            else
                s = s + list.get(i) + str;
        }

        return s;
    }

    /**
     * Surroung given array with pattern
     *
     * @param array The array
     * @param str   The pattern
     *
     * @return The finished string
     */
    public static String surround(String[] array, String str){
        String s = "";
        for(String anArray : array){
            s += str + anArray + str;
        }

        return s;
    }

    /**
     * Surround and insert given array with the two patterns
     *
     * @param array        The array
     * @param insertString The insert pattern
     * @param otherString  The surround pattern
     *
     * @return The finished string
     */
    public static String insertAndSurround(String[] array, String insertString, String otherString){
        String s = "";
        for(int i = 0; i < array.length; i++){
            if(i == array.length - 1)
                s += otherString + array[i] + otherString;
            else
                s += otherString + array[i] + otherString + insertString;
        }

        return s;
    }

}
