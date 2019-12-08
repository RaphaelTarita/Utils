package com.tara.util.persistence.field;

import com.tara.util.annotation.Persistable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            composite.bind(get(on));
        }
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
