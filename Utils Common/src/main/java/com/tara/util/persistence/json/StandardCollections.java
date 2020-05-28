package com.tara.util.persistence.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public class StandardCollections {
    private static final StandardCollections instance = new StandardCollections();

    public static StandardCollections instance() {
        return instance;
    }

    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends Collection>, Supplier<? extends Collection<?>>> standards = new HashMap<>();

    private StandardCollections() {

        standards.put(Collection.class, ArrayList::new);
        standards.put(List.class, ArrayList::new);
        standards.put(Set.class, HashSet::new);
        standards.put(Queue.class, LinkedList::new);
        standards.put(Deque.class, LinkedList::new);
        standards.put(BlockingQueue.class, LinkedBlockingQueue::new);
        standards.put(BlockingDeque.class, LinkedBlockingDeque::new);
    }

    public Supplier<? extends Collection<?>> getStandardImplementorSupplier(@SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass) {
        Supplier<? extends Collection<?>> supplier;
        if (Modifier.isAbstract(collectionClass.getModifiers()) || collectionClass.isInterface()) {
            supplier = standards.get(collectionClass);
        } else {
            supplier = () -> {
                try {
                    return collectionClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    throw new IllegalArgumentException("Exception occurred during collection unmarshalling", ex);
                }
            };
        }
        if (supplier == null) {
            throw new CollectionNotFoundException(collectionClass);
        }
        return supplier;
    }

    @SuppressWarnings("unchecked")
    public Collection<Object> getStandardImplementor(@SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass) {
        return (Collection<Object>) getStandardImplementorSupplier(collectionClass).get();
    }
}