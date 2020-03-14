package ru.itmo.java;

import static java.lang.Math.abs;

public class HashTable {

    private Entry[] F;
    private int threshold;

    private float loadFactor = 0.7f;
    private int initialCapacity;

    private int size = 0;
    private int realSize = 0;

    private static class Entry {
        private Object key;
        private Object value;
        private boolean deleted = false;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }

    HashTable(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        threshold = (int) (initialCapacity * loadFactor);
        F = new Entry[initialCapacity];
    }

    HashTable(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.initialCapacity = initialCapacity;
        threshold = (int) (initialCapacity * loadFactor);
        F = new Entry[initialCapacity];
    }

    private int getHash(Object key) {
        return Math.abs(key.hashCode() % F.length);
    }

    private int search(Object key, boolean findDeleted) {
        int hash = getHash(key);

        while (true) {
            if (F[hash] == null) {
                return hash;
            } else if (F[hash].getKey().equals(key) && (!F[hash].deleted || findDeleted)) {
                return hash;
            }

            hash = (hash + 1) % this.initialCapacity;
        }
    }

    private void resize() {
        Entry[] oldEntry = F;

        initialCapacity *= 2;
        threshold = (int) (initialCapacity * loadFactor);
        size = 0;
        realSize = 0;

        F = new Entry[initialCapacity];

        for (Entry entry : oldEntry) {
            if (entry != null && !entry.deleted) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    Object put(Object key, Object value) {
        int hash = search(key, true);

        if (F[hash] == null) {
            F[hash] = new Entry(key, value);
            size++;
            realSize++;

            if (realSize == threshold) {
                resize();
            }

            return null;
        } else if (F[hash].deleted) {
            F[hash] = new Entry(key, value);
            size++;
            return null;
        } else {
            Object Value = F[hash].getValue();
            F[hash] = new Entry(key, value);
            return Value;
        }
    }

    Object get(Object key) {
        int hash = search(key, false);

        if (F[hash] != null){
            return F[hash].getValue();
        }
        return null;
    }

    Object remove(Object key) {
        int hash = search(key, false);

        if (F[hash] != null) {
            F[hash].deleted = true;
            size--;
            return F[hash].getValue();
        }

        return null;
    }

    int size() {
        return size;
    }
}