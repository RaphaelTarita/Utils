package com.tara.util.container.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class LinkedMap<K, V> implements Map<K, V> {
    private static class Node<K, V> implements Map.Entry<K, V>, Iterator<Node<K, V>> {
        private final K key;
        private V value;
        private Node<K, V> prev;
        private Node<K, V> next;

        public static <K, V> void link(Node<K, V> first, Node<K, V> second) {
            first.next = second;
            second.prev = first;
        }

        public Node(K key, V value, Node<K, V> prev) {
            this.key = key;
            this.value = value;
            prev.next.prev = this;
            this.next = prev.next;
            prev.next = this;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            prev = null;
            next = null;
        }

        public boolean isFirst() {
            return prev == null;
        }

        public boolean isLast() {
            return next == null;
        }

        public Node<K, V> prev() {
            return prev;
        }


        @Override
        public Node<K, V> next() {
            return next;
        }

        @Override
        public boolean hasNext() {
            return !isLast();
        }

        @Override
        public void remove() {
            prev.next = next;
            next.prev = prev;
            this.prev = null;
            this.next = null;
        }

        public Node<K, V> overwriteNext(Node<K, V> node) {
            Node<K, V> old = this.next;
            link(this, node);
            return old;
        }

        public Node<K, V> overwriteNext(K key, V value) {
            return overwriteNext(new Node<>(key, value));
        }

        public void insert(Node<K, V> node) {
            node.next = this.next;
            node.prev = this;
            this.next.prev = node;
            this.next = node;
        }

        public void insert(K key, V value) {
            insert(new Node<>(key, value));
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    private int size;
    private boolean sizeValid;
    private Node<K, V> first;
    private Node<K, V> last;

    public LinkedMap() {
        size = 0;
        sizeValid = true;
        first = null;
        last = null;
    }

    public LinkedMap(Map<? extends K, ? extends V> m) {
        this();
        putAll(m);
    }

    private void add(Node<K, V> node) {
        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            last.overwriteNext(node);
            last = node;
        }
        size++;
    }

    private void add(K key, V value) {
        add(new Node<>(key, value));
    }

    private void calculateSize() {
        if (isEmpty()) {
            size = 0;
        } else {
            int s = 0;
            for (Node<K, V> it = first; it != null; it = it.next()) {
                s++;
            }
            size = s;
        }
        sizeValid = true;
    }

    @Override
    public int size() {
        if (!sizeValid) {
            calculateSize();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return first == null
            || last == null;
    }

    public boolean contains(Object kv, Function<Node<K, V>, Object> getter) {
        for (Node<K, V> it = first; it != null; it = it.next()) {
            if (getter.apply(it).equals(kv)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        return contains(key, Node::getKey);
    }

    @Override
    public boolean containsValue(Object value) {
        return contains(value, Node::getValue);
    }

    @Override
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        for (Node<K, V> it = first; it != null; it = it.next()) {
            if (key.equals(it.getKey())) {
                return it.getValue();
            }
        }
        return null;
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        if (key == null) {
            return null;
        }

        for (Node<K, V> it = first; it != null; it = it.next()) {
            if (key.equals(it.getKey())) {
                V oldval = it.getValue();
                it.setValue(value);
                return oldval;
            }
        }

        add(key, value);
        return null;
    }

    @Override
    public V remove(Object key) {
        if (key == null) {
            return null;
        }

        for (Node<K, V> it = first; it != null; it = it.next()) {
            if (key.equals(it.getKey())) {
                if (it == first) {
                    first = it.next();
                }
                it.remove();
                size--;
                return it.getValue();
            }
        }
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
        sizeValid = true;
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<>(size());

        for (Node<K, V> it = first; it != null; it = it.next()) {
            set.add(it.getKey());
        }
        return set;
    }

    @NotNull
    @Override
    public Collection<V> values() {
        List<V> list = new ArrayList<>(size());

        for (Node<K, V> it = first; it != null; it = it.next()) {
            list.add(it.getValue());
        }
        return list;
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new LinkedHashSet<>(size());

        for (Node<K, V> it = first; it != null; it = it.next()) {
            set.add(it);
        }

        return set;
    }
}
