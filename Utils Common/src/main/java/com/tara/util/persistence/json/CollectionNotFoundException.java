package com.tara.util.persistence.json;

public class CollectionNotFoundException extends NotFoundException {

    public CollectionNotFoundException(Class<?> forClass, String msg, Throwable cause) {
        super(forClass, msg, cause);
    }

    public CollectionNotFoundException(Class<?> forClass, String msg) {
        super(forClass, msg);
    }

    public CollectionNotFoundException(Class<?> forClass) {
        super(forClass);
    }
}
