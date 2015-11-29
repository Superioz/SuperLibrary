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

    public void add(T object){
        if(!list.contains(object))
            this.list.add(object);
    }
    public T get(int index){
        return getCached().get(index);
    }

    public void remove(T object){
        if(list.contains(object))
            this.list.remove(object);
    }

    public void remove(int index){
        this.list.remove(index);
    }

    public List<T> getCached(){
        return list;
    }

    @Override
    public void write(JsonFile file){
        file.write(this.list);
    }

    @Override
    public void from(JsonFile file){
        this.list = file.read(new TypeToken<ArrayList<T>>(){}.getType());

        if(this.list == null)
            this.list = new ArrayList<>();
    }

    @Override
    public Iterator<T> iterator(){
        return list.iterator();
    }
}
