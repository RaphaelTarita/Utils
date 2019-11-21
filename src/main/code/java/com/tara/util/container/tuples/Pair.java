package com.tara.util.container.tuples;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Pair<F, S> {
    private static class PairEntry<FK, SV> implements Map.Entry<FK, SV> {
        private FK firstOrKey;
        private SV secondOrValue;

        public PairEntry(Pair<FK, SV> pair) {
            firstOrKey = pair.first;
            secondOrValue = pair.second;
        }

        @Override
        public FK getKey() {
            return firstOrKey;
        }

        @Override
        public SV getValue() {
            return secondOrValue;
        }

        @Override
        public SV setValue(SV value) {
            secondOrValue = value;
            return secondOrValue;
        }
    }

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

    public Map.Entry<F, S> asMapEntry() {
        return new PairEntry<>(this);
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
