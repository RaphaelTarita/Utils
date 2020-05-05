package com.tara.util.container.tuple;

import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Pair<F, S> implements Map.Entry<F, S>, Mirrorable<Pair<F, S>> {
    protected F first;
    protected S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {
        this(null, null);
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public void first(F first) {
        this.first = first;
    }

    public void second(S second) {
        this.second = second;
    }

    public Twin<String> asStringTwin() {
        return new Twin<>(
            first != null
                ? first.toString()
                : "<null>",
            second != null
                ? second.toString()
                : "<null>"
        );
    }

    public Twin<Boolean> validate(Predicate<F> validateFirst, Predicate<S> validateSecond) {
        return new Twin<>(
            validateFirst.test(first),
            validateSecond.test(second)
        );
    }

    public <FT, ST> Twin<Boolean> validate(BiPredicate<F, FT> validateFirst, FT paramFirst, BiPredicate<S, ST> validateSecond, ST paramSecond) {
        return new Twin<>(
            validateFirst.test(
                first,
                paramFirst
            ),
            validateSecond.test(
                second,
                paramSecond
            )
        );
    }

    @Override
    public F getKey() {
        return first();
    }

    @Override
    public S getValue() {
        return second();
    }

    @Override
    public S setValue(S value) {
        S temp = second;
        second(value);
        return temp;
    }

    @Override
    public Pair<F, S> mirror() {
        return new Pair<>(
            Mirrors.mirror(first),
            Mirrors.mirror(second)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Pair) {
            Pair<?, ?> casted = (Pair<?, ?>) obj;
            return casted.first.equals(first)
                && casted.second.equals(second);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
