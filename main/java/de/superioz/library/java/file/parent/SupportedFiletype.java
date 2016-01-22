package de.superioz.library.java.file.parent;

/**
 * Class created on April in 2015
 */
public enum SupportedFiletype {

    YAML("yml"),
    TEXT("txt"),
    PROPERTIES("properties"),
    XML("xml"),
    JSON("json");

    private String s;
    SupportedFiletype(String name){
        this.s = name;
    }

    // -- Intern methods

    public String getName(){
        return s;
    }
}
