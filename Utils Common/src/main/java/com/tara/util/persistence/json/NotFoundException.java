package com.tara.util.persistence.json;

public class NotFoundException extends RuntimeException {
    private final Class<?> forClass;

    public NotFoundException(Class<?> forClass, String msg, Throwable cause) {
        super(msg, cause);
        this.forClass = forClass;
    }

    public NotFoundException(Class<?> forClass, String msg) {
        super(msg);
        this.forClass = forClass;
    }

    public NotFoundException(Class<?> forClass) {
        this.forClass = forClass;
    }

    @Override
    public String toString() {
        return super.toString() + " (class '" + forClass.getName() + "' is not registered)";
    }
}
