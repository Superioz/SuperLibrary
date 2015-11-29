package de.superioz.library.java.cache;

import com.google.gson.reflect.TypeToken;
import de.superioz.library.java.file.type.JsonFile;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class was created as a part of SuperLibrary
 *
 * @author Superioz
 */
public class MappedCache<T, V> implements Cache, Iterable<T> {

    private HashMap<T, V> map;

    public MappedCache(){
        this.map = new HashMap<>();
    }

    public MappedCache(HashMap<T, V> map){
        this.map = map;
    }

    public HashMap<T, V> getCached(){
        return map;
    }

    public V get(T key){
        if(contains(key))
            return map.get(key);
        return null;
    }

    public void remove(T key){
        if(contains(key))
            map.remove(key);
    }

    public void put(T key, V value){
        map.put(key, value);
    }

    public boolean contains(T key){
        return map.containsKey(key);
    }

    @Override
    public void write(JsonFile file){
        file.write(this.map);
    }

    @Override
    public void from(JsonFile file){
        this.map = file.read(new TypeToken<HashMap<T, V>>(){}.getType());
    }

    @Override
    public Iterator<T> iterator(){
        return map.keySet().iterator();
    }
}
