package com.tara.util.container.map;

import com.tara.util.container.tuple.Pair;
import com.tara.util.helper.decimal.FloatHelper;
import com.tara.util.mirror.Mirrors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ArrayMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INIT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 2.0;

    private Object[] keys;
    private Object[] values;
    private final int initCapacity;
    private final double loadFactor;

    public ArrayMap(int initialCapacity, double loadFactor) {
        initCapacity = Math.max(0, initialCapacity);
        this.loadFactor = Math.max(1.0, loadFactor);
        keys = new Object[initCapacity];
        values = new Object[initCapacity];
    }

    public ArrayMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public ArrayMap(double loadFactor) {
        this(DEFAULT_INIT_CAPACITY, loadFactor);
    }

    public ArrayMap() {
        this(DEFAULT_INIT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public ArrayMap(Map<? extends K, ? extends V> m) {
        initCapacity = m.size();
        loadFactor = DEFAULT_LOAD_FACTOR;
        putAll(m);
    }

    private static boolean contains(Object obj, Object[] in) {
        if (obj == null) {
            return false;
        }
        for (Object keyCandidate : in) {
            if (obj.equals(keyCandidate)) {
                return true;
            }
        }
        return false;
    }

    private boolean full() {
        if (keys.length != values.length) {
            throw new AssertionError("key array and value array have different sizes");
        }
        return size() >= keys.length;
    }

    private void resize(int newsize) {
        int size = size();
        Object[] newKeys = new Object[newsize];
        Object[] newValues = new Object[newsize];
        System.arraycopy(keys, 0, newKeys, 0, size);
        System.arraycopy(values, 0, newValues, 0, size);
        keys = newKeys;
        values = newValues;
    }

    private void grow() {
        resize(
            FloatHelper.epsilonEquals(keys.length, 1.0)
                ? keys.length + 1
                : FloatHelper.floor(keys.length * loadFactor)
        );
    }

    private void shrink() {
        resize(size());
    }

    private void shift(int to, int from) {
        for (int i = 0; i < size() - from; i++) {
            keys[to + i] = keys[from + i];
            values[to + i] = values[from + i];
        }
    }

    private void shift(int to) {
        shift(to, to + 1);
    }

    @Override
    public int size() {
        int size = 0;
        for (Object key : keys) {
            if (key == null) {
                return size;
            }
            size++;
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return contains(key, keys);
    }

    @Override
    public boolean containsValue(Object value) {
        return contains(value, values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        int size = size();
        int count = 0;
        for (Object keyCandidate : keys) {
            if (key.equals(keyCandidate)) {
                return (V) values[count];
            }
            count++;
            if (count >= size) {
                break;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        if (full()) {
            grow();
        }
        int size = size();
        int count = 0;
        for (Object keyCandidate : keys) {
            if (key.equals(keyCandidate)) {
                V oldval = Mirrors.mirror((V) values[count]);
                values[count] = value;
                return oldval;
            }
            count++;
            if (count >= size) {
                break;
            }
        }

        keys[size] = key;
        values[size] = value;

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        if (key == null) {
            return null;
        }
        int size = size();
        int count = 0;
        for (Object keyCandidate : keys) {
            if (keyCandidate.equals(key)) {
                V prev = (V) values[count];
                shift(count);
                return prev;
            }
            count++;
            if (count >= size) {
                break;
            }
        }
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        keys = new Object[initCapacity];
        values = new Object[initCapacity];
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public Set<K> keySet() {
        return Set.of((K[]) keys);
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        return List.of((V[]) values);
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public Set<Entry<K, V>> entrySet() {
        int size = size();
        Set<Entry<K, V>> res = new LinkedHashSet<>(size);
        for (int i = 0; i < size; i++) {
            res.add(
                new Pair<>(
                    (K) keys[i],
                    (V) values[i]
                )
            );
        }
        return res;
    }
}
