/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.helpers;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 *
 * @author pseudo
 * @param <Key>
 * @param <Value>
 */
public class Hashmap<Key, Value> implements Serializable {
    
    private HashMap<Key, Value> map;
    
    /**
     *
     */
    public Hashmap() {
        map = new HashMap<>();
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public Hashmap(Key key, Value value) {
        map = new HashMap<>();
        map.put(key, value);
    }
    
    /**
     *
     * @return
     */
    public HashMap<Key, Value> getMap(){
        return map;
    }
    
    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Hashmap<Key, Value> put(Key key, Value value){
        map.put(key, value);
        return this;
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Value get(Key key){
        return map.get(key);
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Hashmap<Key, Value> remove(Key key){
        map.remove(key);
        return this;
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Value removeAndGet(Key key){
        return map.remove(key);
    }
    
    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    /**
     *
     * @return
     */
    public int size() {
        return map.size();
    }
    
    /**
     *
     * @return
     */
    public Set<Key> keySet() {
        return map.keySet();
    }
    
    /**
     *
     * @param action
     */
    public void forEach(BiConsumer<? super Key, ? super Value> action) {
        map.forEach(action);
    }
    
    /**
     *
     * @param key
     * @param value
     * @param func
     * @return
     */
    public Value merge(Key key, Value value, BiFunction<? super Value, ? super Value, ? extends Value> func) {
        return map.merge(key, value, func);
    }
    
    /**
     *
     * @return
     */
    public Collection<Value> values() {
        return map.values();
    }
    
    /**
     *
     * @param k
     * @return
     */
    public boolean containsKey(Key k) {
        return map.containsKey(k);
    }
}
