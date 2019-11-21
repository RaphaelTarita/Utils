package com.tara.util.mirror;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mirrors {
    private static final String CLONE_METHOD_NAME = "clone";
    private static final String MSG_STARTER = ": ";
    private static final String NO_CLONE = "Object implements Cloneable but does not have a clone() method";
    private static final String NO_CLONE_ACCESS = "Object does not provide access to a clone() method";
    private static final String CLONE_MALFORMED = "Object has a malformed clone() implementation and threw ";

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
}