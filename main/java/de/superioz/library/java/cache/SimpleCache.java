package de.superioz.library.java.cache;

import com.google.gson.reflect.TypeToken;
import de.superioz.library.java.file.type.JsonFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class SimpleCache<T> implements Cache, Iterable<T> {

    protected List<T> list;

    public SimpleCache(){
        this.list = new ArrayList<>();
    }

    public SimpleCache(List<T> list){
        this.list = list;
    }

    /**
     * Adds given object to cache
     *
     * @param object The object
     */
    public void add(T object){
        if(!list.contains(object))
            this.list.add(object);
    }

    /**
     * Gets object from given index
     *
     * @param index The index
     *
     * @return The object
     */
    public T get(int index){
        return getCached().get(index);
    }

    /**
     * Removes given object from cache
     *
     * @param object The object
     */
    public void remove(T object){
        if(list.contains(object))
            this.list.remove(object);
    }

    /**
     * Removes object with given index
     *
     * @param index The index
     */
    public void remove(int index){
        this.list.remove(index);
    }

    // -- Intern methods

    public List<T> getCached(){
        return list;
    }

    @Override
    public void write(JsonFile file){
        file.write(this.list);
    }

    @Override
    public void from(JsonFile file){
        this.list = file.read(new TypeToken<ArrayList<T>>() {}.getType());

        if(this.list == null)
            this.list = new ArrayList<>();
    }

    @Override
    public Iterator<T> iterator(){
        return list.iterator();
    }
}
