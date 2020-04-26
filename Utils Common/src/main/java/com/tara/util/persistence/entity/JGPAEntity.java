package com.tara.util.persistence.entity;

import com.tara.util.annotation.Field;
import com.tara.util.annotation.FieldGET;
import com.tara.util.annotation.FieldSET;
import com.tara.util.annotation.Ignore;
import com.tara.util.annotation.Persistable;
import com.tara.util.annotation.Scan;
import com.tara.util.annotation.processor.GenericScanner;
import com.tara.util.container.tuple.Twin;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class JGPAEntity<VO> {
    private static final Class<? extends Persistable> PERSISTABLE = Persistable.class;
    private static final Class<? extends Scan> SCAN = Scan.class;
    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] enable = new Class[]{
        Field.class,
        FieldGET.class,
        FieldSET.class
    };
    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] disable = new Class[]{Ignore.class};

    private static final String MULTI_GET = "multiple get accessors on one field";
    private static final String MULTI_SET = "multiple set accessors on one field";
    private static final String MISSING_GET = "get accessor is missing";
    private static final String MISSING_SET = "set accessor is missing";
    private static final String WRONG_GETSIG = "wrong get accessor signature";
    private static final String WRONG_SETSIG = "wrong set accessor signature";
    private static final String TYPE_MISMATCH = "get and set accessors do not match";
    private static final String ACCESSOR_NATIVE_INTERSECTION = "found intersection between accessor fields and native fields";

    private final String name;
    private final Class<?> target;
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

    private void resolve(java.lang.reflect.Field f, Map<String, java.lang.reflect.Field> natives) {
        Field annotation = f.getAnnotation(Field.class);
        String fieldName = annotation != null && !annotation.value().isEmpty()
            ? annotation.value()
            : f.getName();

        natives.put(fieldName, f);
    }

    private void checkMaps(Map<String, Twin<Method>> accessors, Map<String, java.lang.reflect.Field> natives) {
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

    private void fieldInit(Map<String, Twin<Method>> accessors, Map<String, java.lang.reflect.Field> natives) {
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

        for (Map.Entry<String, java.lang.reflect.Field> fieldEntry : natives.entrySet()) {
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

    private void collect() {
        Scan scan = target.getAnnotation(SCAN);
        GenericScanner scanner = scan != null
            ? GenericScanner.ignoreScanAnnotation(target, scan.value(), enable, scan.disable())
            : GenericScanner.ignoreScanAnnotation(target, enable, disable);
        scanner.scan();

        Map<String, java.lang.reflect.Field> natives = new HashMap<>();
        Map<String, Twin<Method>> accessors = new HashMap<>();
        for (java.lang.reflect.Field f : scanner.getFields()) {
            resolve(f, natives);
        }
        for (Method m : scanner.getMethods()) {
            resolve(m, accessors);
        }

        checkMaps(accessors, natives);
        fieldInit(accessors, natives);
    }

    private JGPAEntity(Class<?> clazz, String entityName) {
        if (!entityName.isEmpty() && !clazz.isAnnotationPresent(PERSISTABLE)) {
            throw new FieldMappingException(clazz.getName(), "type is not annotated with @Persistable");
        }
        target = clazz;
        String val = !entityName.isEmpty()
            ? entityName
            : target.getAnnotation(PERSISTABLE).value();
        name = val.isEmpty()
            ? target.getSimpleName()
            : val;
    }

    @SuppressWarnings("unchecked")
    public static <VO> JGPAEntity<VO> buildVirtual(Map<String, Object> structure, Class<VO> virtualType, String name) {
        JGPAEntity<VO> res = new JGPAEntity<>(virtualType, name);
        for (Map.Entry<String, Object> kv : structure.entrySet()) {
            JGPAField<VO> fieldResult = null;
            String key = kv.getKey();
            Object value = kv.getValue();
            if (value == null) {
                throw new FieldMappingException(key, "virtual entity structure map contained null value: cannot infer type");
            } else if (value instanceof JGPAField) {
                fieldResult = (JGPAField<VO>) value;
            } else if (value instanceof Map) {
                value = buildVirtual((Map<String, Object>) value, key);
            }

            fieldResult = fieldResult == null
                ? new VirtualField<>(name, key, value.getClass())
                : fieldResult;
            res.fields.put(key, fieldResult);
        }

        return res;
    }

    public static JGPAEntity<Object> buildVirtual(Map<String, Object> structure, String name) {
        return buildVirtual(structure, Object.class, name);
    }

    public JGPAEntity(Class<?> clazz) {
        this(clazz, "");
        collect();
    }

    public JGPAEntity(VO entity) {
        this(entity.getClass());
        bind(entity);
    }

    public void bind(VO entity) {
        if (target.isInstance(entity)) {
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

    public boolean isVirtual() {
        for (JGPAField<VO> field : fields.values()) {
            if (!(field instanceof VirtualField)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasField(String field) {
        return fields.containsKey(field);
    }

    public Object get(String field) {
        JGPAField<VO> entityField = getField(field);
        if (!(entityField instanceof VirtualField)) {
            requireBound();
        }
        if (entityField != null) {
            return entityField.get(entity);
        } else {
            return null;
        }
    }

    public void set(String field, Object value) {
        JGPAField<VO> entityField = getField(field);
        if (!(entityField instanceof VirtualField)) {
            requireBound();
        }
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
        if (!isVirtual()) {
            requireBound();
        }
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
