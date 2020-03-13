package ru.itmo.java;

import java.security.KeyStore;
import java.util.Map;


public class HashTable {

    private Entry[] F;
    private int threshold;

    private float loadFactor = 0.7f;
    private int initialCapacity;

    private int size = 0;
    private int realsize = 0;

    HashTable(int initialCapacity, float loadFactor){
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.threshold = (int)loadFactor * initialCapacity;
        this.F = new Entry[initialCapacity];
    }
    HashTable(int initialCapacity){
        this.initialCapacity = initialCapacity;
        this.threshold = (int)this.loadFactor * initialCapacity;
        this.F = new Entry[initialCapacity];
    }

    private static class Entry{
        private Object key;
        private Object value;
        boolean dead = false;

        Entry(Object key, Object value){
            this.key = key;
            this.value = value;
        }
        Entry(Object key){
            this.key = key;
            this.value = null;
        }
    }

    private int HashFunc(Entry entry){
        return Math.abs(entry.key.hashCode() % this.initialCapacity);
    }

    private void resize() {
        Entry[] oldEntry = F;

        initialCapacity *= 2;
        threshold = (int) (initialCapacity * loadFactor);
        size = 0;
        realsize = 0;

        F = new Entry[initialCapacity];

        for (Entry entry : oldEntry) {
            if (entry != null && !entry.dead) {
                put(entry.key, entry.value);
            }
        }

    }


    Object put(Object key, Object value) {
        var entry = new Entry(key, value);
        var hash = HashFunc(entry);
        while (true) {
            if (F[hash] == null) {
                F[hash] = entry;
                this.size++;
                this.realsize++;

                if (realsize >= threshold) {
                    resize();
                }

                return null;
            }
            else if(F[hash].dead){
                F[hash] = entry;
                this.size++;
                return null;
            }
            else if (F[hash].key.equals(entry.key)) {
                Object Value = F[hash].value;
                F[hash] = entry;
                return Value;
            }
            hash = (hash + 1) % this.initialCapacity;
        }
    }


    Object get(Object key) {
        var hash = HashFunc(new Entry(key));
        var deadhash = hash;
        while (true) {
            if (F[hash] != null && F[hash].key.equals(key) && !F[hash].dead) {
                return F[hash].value;
            }
            hash = (hash + 1) % this.initialCapacity;
            if (hash == deadhash){
                return null;
            }
        }
    }

    Object remove(Object key) {
        var hash = HashFunc(new Entry(key));
        var deadhash = hash;
        while (true) {
            if (F[hash] != null && F[hash].key.equals(key)) {
                F[hash].dead = true;
                this.size--;
                return F[hash].value;
            }
            hash = (hash + 1) % this.initialCapacity;
            if (hash == deadhash){
                return null;
            }
        }
    }

    int size() {
        return size;
    }

}
