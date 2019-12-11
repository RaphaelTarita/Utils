package com.tara.util.persistence.json;

public class MarshallerNotFoundException extends NotFoundException {
    public MarshallerNotFoundException(Class<?> cfor, String msg, Throwable cause) {
        super(cfor, msg, cause);
    }

    public MarshallerNotFoundException(Class<?> cfor, String msg) {
        super(cfor, msg);
    }

    public MarshallerNotFoundException(Class<?> cfor) {
        super(cfor);
    }
}
