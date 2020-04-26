package com.tara.util.persistence.entity;

import java.lang.reflect.Type;

public class VirtualField<VO> extends JGPAField<VO> {
    private static final String TYPE_MISMATCH = "Type mismatch on virtual field 'set()' encountered";
    private Object val;
    private final VirtualParameterizedType pType;

    public VirtualField(String entity, String fieldname, Class<?> type, Type owner, Type[] typeArgs) {
        super(entity, fieldname);
        initComposite(type);
        pType = new VirtualParameterizedType(type, owner, typeArgs);
        val = null;
    }

    public VirtualField(String entity, String fieldname, Class<?> type, Type... typeArgs) {
        this(entity, fieldname, type, null, typeArgs);
    }

    public VirtualField(String entity, String fieldname, Class<?> type) {
        this(entity, fieldname, type, null, null);
    }

    @Override
    public Object get(VO on) {
        // ignore 'on' (virtual)
        return val;
    }

    @Override
    public void set(VO on, Object value) {
        // ignore 'on' (virtual)
        checkType(value);
        value = handleCollection(value);
        val = value;
    }

    private void checkType(Object assign) {
        if (assign != null && !assign.getClass().equals(type)) {
            throw new FieldMappingException(name, entityName, TYPE_MISMATCH);
        }
    }

    @Override
    public Type getGenericType() {
        return pType;
    }
}
