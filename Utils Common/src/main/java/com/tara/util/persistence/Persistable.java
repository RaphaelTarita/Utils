package com.tara.util.persistence;

import com.tara.util.annotation.persistence.FieldGET;
import com.tara.util.annotation.persistence.FieldSET;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface Persistable {
    private static List<Method> filter(Method[] full, Class<? extends Annotation> annotation) {
        List<Method> filtered = new ArrayList<>();
        for (Method method : full) {
            if (method.isAnnotationPresent(annotation)) {
                filtered.add(method);
            }
        }
        return filtered;
    }

    default Object[] data() throws InvocationTargetException, IllegalAccessException {
        List<Method> getters = getters();
        Object[] data = new Object[getters.size()];
        int counter = 0;
        for (Method method : getters) {
            data[counter++] = method.invoke(this);
        }
        return data;
    }

    default List<Method> getters() {
        return filter(this.getClass().getMethods(), FieldGET.class);
    }

    default List<Method> setters() {
        return filter(this.getClass().getMethods(), FieldSET.class);
    }
}
