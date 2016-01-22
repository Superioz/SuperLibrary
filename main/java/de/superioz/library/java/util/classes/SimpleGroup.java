package de.superioz.library.java.util.classes;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SimpleGroup<A, B, C> {

    private A object1;
    private B object2;
    private C object3;

    public SimpleGroup(A object1, B object2, C object3){
        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
    }

    // -- Intern methods

    public A getObject1(){
        return object1;
    }

    public void setObject1(A object1){
        this.object1 = object1;
    }

    public B getObject2(){
        return object2;
    }

    public void setObject2(B object2){
        this.object2 = object2;
    }

    public C getObject3(){
        return object3;
    }

    public void setObject3(C object3){
        this.object3 = object3;
    }

}
