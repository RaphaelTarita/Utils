package com.tara.util.persistence.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class NativeField<VO> extends JGPAField<VO> {
    private static final String PROBLEMATIC_FIELD = "malfunctioning native field in JGPA Field encountered: ";
    private final Field nativeField;

    public NativeField(String entity, String fieldname, Field field) {
        super(entity, fieldname);
        nativeField = field;
        if (Modifier.isPrivate(nativeField.getModifiers())) {
            nativeField.setAccessible(true);
        }
        initComposite(nativeField.getType());
    }

    @Override
    public Object get(VO on) {
        try {
            return nativeField.get(on);
        } catch (IllegalAccessException ex) {
            throw new FieldMappingException(name, entityName, PROBLEMATIC_FIELD + ex.toString());
        }
    }

    @Override
    public void set(VO on, Object value) {
        value = handleCollection(value);
        try {
            nativeField.set(on, value);
        } catch (IllegalAccessException ex) {
            throw new FieldMappingException(name, entityName, PROBLEMATIC_FIELD + ex.toString());
        }
    }

    @Override
    public Type getGenericType() {
        return nativeField.getGenericType();
    }
}
