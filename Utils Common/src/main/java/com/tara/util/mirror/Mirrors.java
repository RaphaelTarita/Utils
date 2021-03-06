package com.tara.util.mirror;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Mirrors {
    private static final String CLONE_METHOD_NAME = "clone";
    private static HashMap<Class<?>, Function<Object, ?>> supplierRegistry = new HashMap<>();

    private Mirrors() {
    }

    public static <C> C port(C toPort, int portIterations) {
        C res = mirror(toPort);
        for (int i = 1; i < portIterations; i++) {
            res = mirror(res);
        }
        return res;
    }

    public static <C> C mirror(Mirrorable<C> toMirror) {
        return toMirror.mirror();
    }

    public static <A> A[] mirror(A[] toMirror) {
        for (int i = 0; i < toMirror.length; i++) {
            toMirror[i] = mirror(toMirror[i]);
        }

        return toMirror.clone();
    }

    public static <L> List<L> mirror(Collection<L> toMirror) {
        if (toMirror.isEmpty()) {
            return new ArrayList<>(toMirror);
        } else {
            List<L> result = new ArrayList<>();
            for (L listEntry : toMirror) {
                result.add(mirror(listEntry));
            }
            return result;
        }
    }

    public static <K, V> Map<K, V> mirror(Map<K, V> toMirror) {
        if (toMirror.isEmpty()) {
            return new HashMap<>(toMirror);
        } else {
            HashMap<K, V> result = new HashMap<>();
            for (Map.Entry<K, V> entry : toMirror.entrySet()) {
                result.put(
                        mirror(entry.getKey()),
                        mirror(entry.getValue())
                );
            }
            return result;
        }
    }

    @SuppressWarnings("unchecked")
    public static <C> C mirror(C toMirror) {
        if (toMirror instanceof Mirrorable) {
            return ((Mirrorable<C>) toMirror).mirror();
        } else if (toMirror instanceof Object[]) {
            return (C) mirror((Object[]) toMirror);
        } else if (toMirror instanceof Collection) {
            return (C) mirror((Collection<?>) toMirror);
        } else if (toMirror instanceof Map) {
            return (C) mirror((Map<?, ?>) toMirror);
        } else if (supplierRegistry.containsKey(toMirror.getClass())) {
            return copyWithRegisteredSupplier(toMirror);
        } else {
            return cloneGeneric(toMirror);
        }
    }

    @SuppressWarnings("unchecked")
    public static <C> C cloneGeneric(C generic) {
        if (generic instanceof Cloneable) {
            try {
                return (C) generic.getClass().getMethod(CLONE_METHOD_NAME).invoke(generic);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                return generic;
            }
        } else {
            return generic;
        }
    }

    public static <C> void registerSupplier(Class<C> clazz, UnaryOperator<C> supplier) {
        supplierRegistry.put(clazz, composeSupplier(clazz, supplier));
    }

    public static <C> void unregisterSupplier(Class<C> clazz) {
        supplierRegistry.remove(clazz);
    }

    public static void unregisterAllSuppliers() {
        supplierRegistry = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    private static <C> C copyWithRegisteredSupplier(C toCopy) {
        return (C) supplierRegistry.get(toCopy.getClass()).apply(toCopy);
    }

    @SuppressWarnings("unchecked")
    private static <C> Function<Object, C> composeSupplier(Class<C> clazz, UnaryOperator<C> copyConstructor) {
        return (Object o) -> {
            if (clazz.isInstance(o)) {
                return copyConstructor.apply((C) o);
            } else {
                throw new ClassCastException("Object" + o.toString() + "is not instance of Class " + clazz.toString());
            }
        };
    }
}