/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 *
 * @author pseudo
 */
public class Hashmap<Key, Value> implements Serializable {
    
    private HashMap<Key, Value> map;
    
    public Hashmap() {
        map = new HashMap<>();
    }
    
    public Hashmap(Key key, Value value) {
        map = new HashMap<>();
        map.put(key, value);
    }
    
    
    public HashMap<Key, Value> getMap(){
        return map;
    }
    
    public Hashmap<Key, Value> put(Key key, Value value){
        map.put(key, value);
        return this;
    }
    
    public Value get(Key key){
        return map.get(key);
    }
    
    public Hashmap<Key, Value> remove(Key key){
        map.remove(key);
        return this;
    }
    
    public Value removeAndGet(Key key){
        return map.remove(key);
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public int size() {
        return map.size();
    }
    
    public Set<Key> keySet() {
        return map.keySet();
    }
    
    public void forEach(BiConsumer<? super Key, ? super Value> action) {
        map.forEach(action);
    }
    
    public Value merge(Key key, Value value, BiFunction<? super Value, ? super Value, ? extends Value> func) {
        return map.merge(key, value, func);
    }
    
    public Collection<Value> values() {
        return map.values();
    }
    
    public boolean containsKey(Key k) {
        return map.containsKey(k);
    }
}
