package com.tara.util.container.collection;

public enum Orientation {
    X('x'),
    Y('y');

    private final char symbol;

    Orientation(char sym) {
        symbol = sym;
    }

    public char symbol() {
        return symbol;
    }
}
