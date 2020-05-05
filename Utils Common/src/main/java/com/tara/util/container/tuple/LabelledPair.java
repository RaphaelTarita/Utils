package com.tara.util.container.tuple;

import com.tara.util.container.label.Labelled;

import java.util.Objects;

public class LabelledPair<F, S> extends Pair<F, S> {
    private static class PairLabel<T> implements Labelled<T> {
        private T value;
        private final String label;

        public PairLabel(T value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String label() {
            return label;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public void value(T newV) {
            value = newV;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Labelled)
                && ((Labelled<?>) obj).value().equals(value)
                && ((Labelled<?>) obj).label().equals(label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return label + ": " + valOrNull();
        }

        private String valOrNull() {
            return value != null
                ? value.toString()
                : "<null>";
        }
    }

    private final String labelFirst;
    private final String labelSecond;

    public LabelledPair(F first, String labelFirst, S second, String labelSecond) {
        super(first, second);
        this.labelFirst = labelFirst;
        this.labelSecond = labelSecond;
    }

    public LabelledPair(String labelFirst, String labelSecond) {
        this(null, labelFirst, null, labelSecond);
    }

    public Object get(String label) {
        if (label.equals(labelFirst)) {
            return first;
        } else if (label.equals(labelSecond)) {
            return second;
        } else {
            return null;
        }
    }

    public String firstLabel() {
        return labelFirst;
    }

    public String secondLabel() {
        return labelSecond;
    }

    public Labelled<F> firstLabelled() {
        return new PairLabel<>(first, labelFirst);
    }

    public Labelled<S> secondLabelled() {
        return new PairLabel<>(second, labelSecond);
    }

    public Pair<Labelled<F>, Labelled<S>> asPair() {
        return new Pair<>(
            firstLabelled(),
            secondLabelled()
        );
    }

    public Twin<String> asLabelTwin() {
        return new Twin<>(labelFirst, labelSecond);
    }

    @Override
    public LabelledPair<F, S> mirror() {
        Pair<F, S> superPair = super.mirror();
        return new LabelledPair<>(
            superPair.first,
            labelFirst,
            superPair.second,
            labelSecond
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof LabelledPair) {
            return ((LabelledPair<?, ?>) obj).asPair().equals(this.asPair());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), labelFirst, labelSecond);
    }
}
