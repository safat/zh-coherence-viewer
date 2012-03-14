package com.zh.coherence.viewer.utils;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.03.12
 * Time: 0:26
 */
public class LRUList<T> extends LinkedList<T> {
    private int size;

    public LRUList(int size) {
        this.size = size;
    }

    public boolean add(T value) {
        if (super.contains(value)) {
            super.remove(value);
        } else {
            if (this.size() == size) {
                super.removeLast();
            }
        }
        super.addFirst(value);

        return true;
    }
}
