package de.superioz.library.java.util;

import java.util.Arrays;
import java.util.List;

/**
 * Class created on April in 2015
 */
public class AlphabetUtils {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Returns an alphabetic char from number
     * e.g: a = 1; z = 26
     */
    public static String getFromNumber(int number){
        if(number <= 0 || number > (26*2)){
            return "null";
        }

        return ALPHABET.split("")[number-1];
    }

    /**
     * Returns the index of given char in ALPHABET
     */
    public static int getFromChar(String ch){
        List<String> alph = Arrays.asList(ALPHABET.split(""));

        if(alph.contains(ch)){
            return alph.indexOf(ch)+1;
        }
        return -1;
    }

}
