/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author pseudo
 */
public class Arraylist<T> extends ArrayList<T> {
    
    public Arraylist() {
        super();
    }
    
    public Arraylist(T value) {
        add(value);
    }
    
    public Arraylist(List<T> values) {
        super(values);
    }

    public Arraylist<T> putIn(T value){
        add(value);
        return this;
    }
    
    public Arraylist<T> putIn(int index, T value){
        add(index, value);
        return this;
    }
  
    public Arraylist<T> removeFrom(T value){
        remove(value);
        return this;
    }
    
    public Arraylist<T> removeFrom(int index){
        remove(index);
        return this;
    }
    
    public void forEachInOrder(Consumer<? super T> c){
        for (int i = 0; i < size(); i++){
            c.accept(get(i));
        }
    }
    
}
