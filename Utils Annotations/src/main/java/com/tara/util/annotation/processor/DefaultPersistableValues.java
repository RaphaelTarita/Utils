package com.tara.util.annotation.processor;

import com.tara.util.annotation.Persistable;

@Persistable
public class DefaultPersistableValues {
    private static final Persistable PERSISTABLE = DefaultPersistableValues.class.getAnnotation(Persistable.class);

    public static final String VALUE = PERSISTABLE.value();

    public static final String ID_NAME = PERSISTABLE.idName();

    private DefaultPersistableValues() {
    }
}
