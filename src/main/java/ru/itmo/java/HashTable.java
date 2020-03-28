package ru.itmo.java;

import static java.lang.Math.abs;

public class HashTable {

    private Entry[] entry;
    private int threshold;

    private float loadFactor = 0.7f;
    private int initialCapacity;

    private int size = 0;
    private int realSize = 0;

    private int getHash(Object key) {
        return Math.abs(key.hashCode() % entry.length);
    }

    private int search(Object key, boolean findDeleted) {
        int hash = getHash(key);

        while (true) {
            if (entry[hash] == null) {
                return hash;
            } else if (entry[hash].getKey().equals(key) && (!entry[hash].deleted || findDeleted)) {
                return hash;
            }

            hash = (hash + 1) % initialCapacity;
        }
    }

    private void resize() {
        Entry[] oldEntry = entry;

        initialCapacity *= 2;
        //без каста выдаёт ошибку
        threshold = (int) (initialCapacity * loadFactor);
        size = 0;
        realSize = 0;

        entry = new Entry[initialCapacity];

        for (Entry entry : oldEntry) {
            if (entry != null && !entry.deleted) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    HashTable(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        threshold = (int) (initialCapacity * loadFactor);
        entry = new Entry[initialCapacity];
    }

    HashTable(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.initialCapacity = initialCapacity;
        threshold = (int) (initialCapacity * loadFactor);
        entry = new Entry[initialCapacity];
    }

    Object put(Object key, Object value) {
        int hash = search(key, true);

        if (entry[hash] == null) {
            entry[hash] = new Entry(key, value);
            size++;
            realSize++;

            if (realSize >= threshold) {
                resize();
            }

            return null;
        } if (entry[hash].deleted) {
            entry[hash] = new Entry(key, value);
            size++;
            return null;
        } else {
            Object _value = entry[hash].getValue();
            entry[hash] = new Entry(key, value);
            return _value;
        }
    }

    Object get(Object key) {
        int hash = search(key, false);

        if (entry[hash] != null){
            return entry[hash].getValue();
        }
        return null;
    }

    Object remove(Object key) {
        int hash = search(key, false);

        if (entry[hash] != null) {
            entry[hash].deleted = true;
            size--;
            return entry[hash].getValue();
        }

        return null;
    }

    int size() {
        return size;
    }

    private final static class Entry {
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
}