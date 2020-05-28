package com.tara.util.persistence.http.socket;

import java.util.function.Function;

@FunctionalInterface
public interface ExceptionHandler<E extends Exception> {
    void handle(E ex);

    static <E extends Exception> ExceptionHandler<E> panic() {
        return ex -> {
            throw new RuntimeException(ex);
        };
    }

    static <E extends Exception, T extends RuntimeException> ExceptionHandler<E> panic(T throwEx) {
        return ex -> {
            throw throwEx;
        };
    }

    static <E extends Exception, T extends RuntimeException> ExceptionHandler<E> panic(Function<E, T> exceptionSupplier) {
        return ex -> {
            throw exceptionSupplier.apply(ex);
        };
    }

    static <E extends Exception> ExceptionHandler<E> sneak() {
        return panic((Function<E, UncheckedThreadException>) UncheckedThreadException::new);
    }
}