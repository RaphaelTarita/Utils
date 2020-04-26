package com.tara.util.persistence.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class AccessorField<VO> extends JGPAField<VO> {
    private static final String PROBLEMATIC_ACCESSOR = "malfunctioning accessor method in JGPA Field encountered: ";
    private final Method getAccessor;
    private final Method setAccessor;

    public AccessorField(String entity, String fieldname, Method get, Method set) {
        super(entity, fieldname);
        getAccessor = get;
        setAccessor = set;
        initComposite(getAccessor.getReturnType());
    }

    @Override
    public Object get(VO on) {
        Object value;
        try {
            value = getAccessor.invoke(on);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new FieldMappingException(name, entityName, PROBLEMATIC_ACCESSOR + ex.toString());
        }
        return value;
    }

    @Override
    public void set(VO on, Object value) {
        value = handleCollection(value);
        try {
            setAccessor.invoke(on, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new FieldMappingException(name, entityName, PROBLEMATIC_ACCESSOR + ex.toString());
        }
    }

    @Override
    public Type getGenericType() {
        return getAccessor.getGenericReturnType();
    }


}
