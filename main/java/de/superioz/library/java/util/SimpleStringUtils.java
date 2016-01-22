package de.superioz.library.java.util;

import de.superioz.library.java.util.list.ListUtil;

/**
 * This class was created as a part of SuperFramework
 *
 * @author Superioz
 */
public class SimpleStringUtils {

    /**
     * Uppers first letter of given normal string
     *
     * @param name The string
     *
     * @return The finished string
     */
    public static String upperFirstLetter(String name){
        return name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Makes the first letter upper case
     *
     * @param spacedName The name
     * @param spacer     The spacer
     *
     * @return The finished string
     */
    public static String upperFirstLetterSpaced(String spacedName, String spacer){
        if(!spacedName.contains(spacer)){
            return upperFirstLetter(spacedName);
        }

        String[] strings = spacedName.split(spacer);

        for(int i = 0; i < strings.length; i++){
            strings[i] = upperFirstLetter(strings[i]);
        }

        return ListUtil.insert(strings, " ");
    }

    /**
     * Checks if given string is an integer
     *
     * @param str The string
     *
     * @return The result
     */
    public static boolean isInteger(String str){
        if(str == null){
            return false;
        }
        int length = str.length();
        if(length == 0){
            return false;
        }
        int i = 0;
        if(str.charAt(0) == '-'){
            if(length == 1){
                return false;
            }
            i = 1;
        }
        for(; i < length; i++){
            char c = str.charAt(i);
            if(c <= '/' || c >= ':'){
                return false;
            }
        }
        return true;
    }

}
