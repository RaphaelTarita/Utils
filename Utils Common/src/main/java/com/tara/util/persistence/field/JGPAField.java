package com.tara.util.persistence.field;

import com.tara.util.annotation.Persistable;
import com.tara.util.persistence.json.StandardCollections;

import java.lang.reflect.Type;
import java.util.Collection;

public abstract class JGPAField<VO> {
    protected String entityName;
    protected String name;
    protected Class<?> type;
    private JGPAEntity<Object> composite;

    protected JGPAField(String entity, String fieldname) {
        entityName = entity;
        name = fieldname;
    }

    protected void initComposite(Class<?> foundType) {
        type = foundType;
        if (type.isAnnotationPresent(Persistable.class)) {
            composite = new JGPAEntity<>(type);
        }
    }

    public abstract Object get(VO on);

    public abstract void set(VO on, Object value);

    public abstract Type getGenericType();

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

    protected static Object handleCollection(Object collection) {
        if (collection instanceof Collection) {
            Collection<?> valueCollection = (Collection<?>) collection;
            Collection<Object> target = StandardCollections.instance().getStandardImplementor(valueCollection.getClass());
            target.addAll(valueCollection);
            return target;
        }
        return collection;
    }
}
