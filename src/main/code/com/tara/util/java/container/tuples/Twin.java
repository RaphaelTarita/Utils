package com.tara.util.java.container.tuples;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Twin<T> extends Pair<T, T> {
    public Twin(T first, T second) {
        super(first, second);
    }

    public Twin(T both) {
        super(both, both);
    }

    public void swap() {
        T cache = first;
        secondTakeover();
        second = cache;
    }

    public void firstTakeover() {
        second = first;
    }

    public void secondTakeover() {
        first = second;
    }

    @SuppressWarnings("unchecked")
    public T[] asArray() {
        return (T[]) new Object[]{
                first,
                second
        };
    }

    public List<T> asList() {
        return List.of(
                first,
                second
        );
    }

    public Set<T> asSet() {
        return Set.of(
                first,
                second
        );
    }

    public Twin<Boolean> validate(Predicate<T> validateBoth) {
        return new Twin<>(
                validateBoth.test(first),
                validateBoth.test(second)
        );
    }

    public <BT> Twin<Boolean> validate(BiPredicate<T, BT> validateBoth, BT paramBoth) {
        return new Twin<>(
                validateBoth.test(first, paramBoth),
                validateBoth.test(second, paramBoth)
        );
    }
}
