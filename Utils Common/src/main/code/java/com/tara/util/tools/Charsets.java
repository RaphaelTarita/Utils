package com.tara.util.tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Charsets {
    ALPHA_LOWER("abcdefghijklmnopqrstuvwxyz"),
    ALPHA_UPPER("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    NUMERICAL("0123456789"),
    OPERATORS("+-*/="),
    SPACES(" \n\t"),
    PUNCTUATIONS(".?!-"),

    EXTRAS(OPERATORS.charsetString + PUNCTUATIONS.charsetString + "#@~_'"),
    MATHEMATICAL(NUMERICAL.charsetString + OPERATORS.charsetString),
    ALPHA(ALPHA_LOWER.charsetString + ALPHA_UPPER.charsetString),
    ALPHANUM(ALPHA.charsetString + NUMERICAL.charsetString),
    TEXTUAL(ALPHANUM.charsetString + SPACES.charsetString + PUNCTUATIONS.charsetString),
    DEFAULT(ALPHANUM.charsetString + EXTRAS.charsetString);

    private final String charsetString;
    private final Set<Character> charset;

    Charsets(String charsetString) {
        this.charsetString = charsetString;
        this.charset = toSet(this.charsetString);
    }

    public Set<Character> charset() {
        return charset;
    }

    public String stringval() {
        return charsetString;
    }

    public static List<Character> toList(String str) {
        return List.of(
                str
                        .chars()
                        .mapToObj(c -> (char) c)
                        .toArray(Character[]::new)
        );
    }

    public static Set<Character> toSet(String str) {
        return new HashSet<>(toList(str));
    }

    public static Set<Character> compose(Charsets... charsets) {
        StringBuilder res = new StringBuilder();
        for (Charsets predefined : charsets) {
            res.append(predefined.charsetString);
        }
        return toSet(res.toString());
    }
}

