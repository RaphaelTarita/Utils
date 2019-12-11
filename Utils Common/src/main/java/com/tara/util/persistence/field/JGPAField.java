package com.tara.util.persistence.field;

import com.tara.util.annotation.Persistable;
import com.tara.util.persistence.json.StandardCollections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class JGPAField<VO> {
    private static final String PROBLEMATIC_ACCESSOR = "malfunctioning accessor method in JGPA Field encountered: ";

    private String entityName;
    private String name;
    private Class<?> type;
    private Method getAccessor;
    private Method setAccessor;
    private JGPAEntity<Object> composite;

    protected JGPAField(String entity, String fieldname, Method get, Method set) {
        entityName = entity;
        name = fieldname;
        getAccessor = get;
        setAccessor = set;
        type = getAccessor.getReturnType();
        if (type.isAnnotationPresent(Persistable.class)) {
            composite = new JGPAEntity<>(type);
        }
    }

    public Method getter() {
        return getAccessor;
    }

    public Method setter() {
        return setAccessor;
    }

    public Object get(VO on) {
        Object value;
        try {
            value = getAccessor.invoke(on);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new FieldMappingException(name, entityName, PROBLEMATIC_ACCESSOR + ex.toString());
        }
        return value;
    }

    public void set(VO on, Object value) {
        if (value instanceof Collection) {
            Collection<?> valueCollection = (Collection<?>) value;
            Collection<Object> target = StandardCollections.instance().getStandardImplementor(valueCollection.getClass());
            target.addAll(valueCollection);
            value = target;
        }
        try {
            setAccessor.invoke(on, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new FieldMappingException(name, entityName, PROBLEMATIC_ACCESSOR + ex.toString());
        }
    }

    public boolean isComposite() {
        return composite != null;
    }

    public JGPAEntity<?> getComposite() {
        return composite;
    }

    public void bindComposite(VO on) {
        if (isComposite()) {
            Object compositeObject = get(on);
            if (compositeObject == null) {
                composite.bindEmpty();
            } else {
                composite.bind(compositeObject);
            }
        }
    }

    public JGPAEntity<?> getBoundComposite(VO on) {
        bindComposite(on);
        return getComposite();
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
