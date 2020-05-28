package com.tara.util.persistence.http.socket;

public class UncheckedThreadException extends RuntimeException {
    public UncheckedThreadException(Exception ex) {
        super(ex);
    }

    public Exception inner() {
        return (Exception) super.getCause();
    }
}