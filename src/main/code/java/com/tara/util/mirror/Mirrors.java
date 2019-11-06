package com.tara.util.mirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mirrors {
    private Mirrors() {
    }

    public static <C> C port(Mirrorable<C> toPort, int mirrors) {
        if (mirrors == 0) {
            return toPort.get();
        }
        Mirrorable<C> currentPortLocation = toPort.mirrorWrap();
        for (int i = 1; i < mirrors; i++) {
            currentPortLocation = currentPortLocation.mirrorWrap();
        }
        return currentPortLocation.get();
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

    public static <L> List<L> mirror(List<L> toMirror) {
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
        } else {
            return toMirror;
        }
    }
}
