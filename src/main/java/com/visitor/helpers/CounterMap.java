/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.helpers;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * @param <Key>
 * @param <Value>
 * @author pseudo
 */
public class CounterMap<Key> extends HashMap<Key, Integer> {


	/**
	 *
	 */
	public CounterMap () {
		super();
	}

	/**
	 * @param key
	 * @param value
	 */
	public CounterMap (Key key, Integer value) {
		super.put(key, value);
	}

	public Integer add (Key key, Integer value) {
		return super.put(key, super.getOrDefault(key, 0) + value);
	}

	public Integer add (Key key) {
		return add(key, 1);
	}

	public boolean contains (Key key) {
		return super.containsKey(key);
	}

	public Integer decrease (Key key, Integer value) {
		if (super.getOrDefault(key, 0) <= value) {
			return super.remove(key);
		} else {
			return super.put(key, super.get(key) - value);
		}
	}

	public void forEachKey (Consumer<? super Key> consumer) {
		super.keySet().forEach(consumer);
	}

	public void merge (CounterMap<Key> map) {
		map.forEach((key, count) -> add(key, count));
	}
}
