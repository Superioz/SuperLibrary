package de.superioz.library.java.util.classes;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SimplePair<A, B> {

    private A type1;
    private B type2;

    public SimplePair(A type1, B type2){
        this.type1 = type1;
        this.type2 = type2;
    }

    // -- Intern methods

    public A getType1(){
        return type1;
    }

    public void setType1(A type1){
        this.type1 = type1;
    }

    public B getType2(){
        return type2;
    }

    public void setType2(B type2){
        this.type2 = type2;
    }
}
