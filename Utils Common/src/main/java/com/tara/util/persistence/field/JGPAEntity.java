package com.tara.util.persistence.field;

import com.tara.util.annotation.FieldGET;
import com.tara.util.annotation.FieldSET;
import com.tara.util.annotation.Persistable;
import com.tara.util.container.tuple.Twin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class JGPAEntity<VO> {
    private static final String MULTI_GET = "multiple get accessors on one field";
    private static final String MULTI_SET = "multiple set accessors on one field";
    private static final String MISSING_GET = "get accessor is missing";
    private static final String MISSING_SET = "set accessor is missing";
    private static final String WRONG_GETSIG = "wrong get accessor signature";
    private static final String WRONG_SETSIG = "wrong set accessor signature";
    private static final String TYPE_MISMATCH = "get and set accessors do not match";
    private static final String ACCESSOR_NATIVE_INTERSECTION = "found intersection between accessor fields and native fields";

    private String name;
    private Class<?> target;
    private VO entity;
    private Map<String, JGPAField<VO>> fields;

    private void resolve(Method m, Map<String, Twin<Method>> accessors) {
        if (m.isAnnotationPresent(FieldGET.class)) {
            String fieldName = m.getAnnotation(FieldGET.class).value();
            Twin<Method> accessorTwin = accessors.get(fieldName);
            if (accessorTwin != null) {
                if (accessorTwin.first() != null) {
                    throw new FieldMappingException(fieldName, name, MULTI_GET);
                }
                accessorTwin.first(m);
            } else {
                accessors.put(fieldName, new Twin<>(m, null));
            }
        } else if (m.isAnnotationPresent(FieldSET.class)) {
            String fieldName = m.getAnnotation(FieldSET.class).value();
            Twin<Method> accessorTwin = accessors.get(fieldName);
            if (accessorTwin != null) {
                if (accessorTwin.second() != null) {
                    throw new FieldMappingException(fieldName, name, MULTI_SET);
                }
                accessorTwin.second(m);
            } else {
                accessors.put(fieldName, new Twin<>(null, m));
            }
        }
    }

    private void resolve(Field f, Map<String, Field> natives) {
        if (f.isAnnotationPresent(com.tara.util.annotation.Field.class)) {
            natives.put(f.getAnnotation(com.tara.util.annotation.Field.class).value(), f);
        }
    }

    private void checkMaps(Map<String, Twin<Method>> accessors, Map<String, Field> natives) {
        Set<String> duplicates = new HashSet<>(natives.keySet());
        duplicates.retainAll(accessors.keySet());
        if (duplicates.size() > 0) {
            throw new FieldMappingException(duplicates.iterator().next(), name, ACCESSOR_NATIVE_INTERSECTION +
                    (duplicates.size() > 1
                            ? " (+ " + (duplicates.size() - 1) + " more related issues"
                            : "")
            );
        }

        for (Map.Entry<String, Twin<Method>> accessorTwin : accessors.entrySet()) {
            Method getter = accessorTwin.getValue().first();
            Method setter = accessorTwin.getValue().second();
            if (getter == null) {
                throw new FieldMappingException(accessorTwin.getKey(), name, MISSING_GET);
            }
            if (setter == null) {
                throw new FieldMappingException(accessorTwin.getKey(), name, MISSING_SET);
            }

            if (getter.getReturnType().equals(void.class) || getter.getParameterCount() > 0) {
                throw new FieldMappingException(accessorTwin.getKey(), name, WRONG_GETSIG);
            }
            if (setter.getParameterCount() != 1) {
                throw new FieldMappingException(accessorTwin.getKey(), name, WRONG_SETSIG);
            }

            if (!getter.getReturnType().equals(setter.getParameterTypes()[0])) {
                throw new FieldMappingException(accessorTwin.getKey(), name, TYPE_MISMATCH);
            }
        }
    }

    private void fieldInit(Map<String, Twin<Method>> accessors, Map<String, Field> natives) {
        fields = new HashMap<>(accessors.entrySet().size() + natives.entrySet().size());
        for (Map.Entry<String, Twin<Method>> accessorTwin : accessors.entrySet()) {
            fields.put(
                    accessorTwin.getKey(),
                    new AccessorField<>(
                            name,
                            accessorTwin.getKey(),
                            accessorTwin.getValue().first(),
                            accessorTwin.getValue().second()
                    )
            );
        }

        for (Map.Entry<String, Field> fieldEntry : natives.entrySet()) {
            fields.put(
                    fieldEntry.getKey(),
                    new NativeField<>(
                            name,
                            fieldEntry.getKey(),
                            fieldEntry.getValue()
                    )
            );
        }
    }

    public JGPAEntity(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Persistable.class)) {
            throw new FieldMappingException(clazz.getName(), "type is not annotated with @Persistable");
        }
        name = clazz.getAnnotation(Persistable.class).value();
        target = clazz;
        Class<?> current = clazz;
        Map<String, Field> natives = new HashMap<>();
        Map<String, Twin<Method>> accessors = new HashMap<>();
        while (current != Object.class) {
            List<Field> currentFields = new ArrayList<>(Arrays.asList(current.getDeclaredFields()));
            List<Method> currentMethods = new ArrayList<>(Arrays.asList(current.getDeclaredMethods()));
            for (Method m : currentMethods) {
                resolve(m, accessors);
            }
            for (Field f : currentFields) {
                resolve(f, natives);
            }
            current = current.getSuperclass();
        }
        checkMaps(accessors, natives);
        fieldInit(accessors, natives);
    }

    public JGPAEntity(VO entity) {
        this(entity.getClass());
        bind(entity);
    }

    public void bind(VO entity) {
        if (target.isInstance(entity)) {
            VO temp = this.entity;
            this.entity = entity;
        } else {
            throw new IllegalArgumentException("entity binding type mismatch, required: " + target.getName() + ", found: " + entity.getClass().getName());
        }
    }

    @SuppressWarnings("unchecked")
    public VO getEmpty() {
        try {
            return (VO) target.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + target.toString() + " (" + name + ") has no default constructor");
        }
    }

    public void bindEmpty() {
        bind(getEmpty());
    }

    public VO detach() {
        VO temp = entity;
        entity = null;
        return temp;
    }

    public boolean bound() {
        return entity != null;
    }

    public boolean detached() {
        return entity == null;
    }

    public boolean hasField(String field) {
        return fields.containsKey(field);
    }

    public Object get(String field) {
        requireBound();
        JGPAField<VO> entityField = getField(field);
        if (entityField != null) {
            return entityField.get(entity);
        } else {
            return null;
        }
    }

    public void set(String field, Object value) {
        requireBound();
        JGPAField<VO> entityField = getField(field);
        if (entityField != null) {
            entityField.set(entity, value);
        }
    }

    public JGPAField<VO> getField(String name) {
        return fields.get(name);
    }

    public boolean isCompositeField(String field) {
        return getField(field).isComposite();
    }

    public JGPAEntity<?> getCompositeField(String field) {
        return getField(field).getComposite();
    }

    public JGPAEntity<?> getBoundCompositeField(String field) {
        getField(field).bindComposite(entity);
        return getCompositeField(field);
    }

    public String name() {
        return name;
    }

    public Class<?> target() {
        return target;
    }

    private void requireBound() {
        if (detached()) {
            throw new IllegalStateException("JGPA Entity is not bound");
        }
    }

    public List<JGPAField<VO>> getFields() {
        return new ArrayList<>(fields.values());
    }

    public Map<String, Object> getValueMap() {
        requireBound();
        Map<String, Object> values = new HashMap<>(fields.size());
        for (Map.Entry<String, JGPAField<VO>> entry : fields.entrySet()) {
            String k = entry.getKey();
            JGPAField<VO> v = entry.getValue();
            if (entry.getValue().isComposite()) {
                values.put(k, v.getBoundComposite(entity).getValueMap());
            } else {
                values.put(k, v.get(entity));
            }
        }
        return values;
    }

    public VO getVO() {
        if (detached()) {
            return getEmpty();
        }
        return entity;
    }
}
