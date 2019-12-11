package com.tara.util.helper.lambda;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
    R apply(T param) throws E;
}
