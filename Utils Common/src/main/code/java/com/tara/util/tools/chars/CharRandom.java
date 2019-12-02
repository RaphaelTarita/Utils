package com.tara.util.tools.chars;

import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class CharRandom implements Mirrorable<CharRandom> {
    private static final Random GENERAL_PURPOSE = new Random();
    private static final List<Character> DEFAULT_PATTERN = Charsets.toList(Charsets.DEFAULT.stringval());

    private Random r;
    private final List<Character> chars;

    private static int randBetween(Random r, int lbound, int ubound) {
        return r.nextInt((ubound - lbound) + 1) + lbound;
    }

    private static int randBetween(int lbound, int ubound) {
        return randBetween(GENERAL_PURPOSE, lbound, ubound);
    }

    private int charsizeRandom() {
        return r.nextInt(chars.size());
    }

    private CharRandom(List<Character> charset) {
        r = new Random();
        chars = charset;
    }

    public CharRandom(Set<Character> charset) {
        this(new ArrayList<>(charset));
    }

    public CharRandom(Charsets predefined) {
        this(predefined.charset());
    }

    public CharRandom(String charset) {
        this(Charsets.toSet(charset));
    }

    public CharRandom() {
        this(DEFAULT_PATTERN);
    }

    public void refresh() {
        r = new Random();
    }

    public Character rand() {
        return chars.get(charsizeRandom());
    }

    public String randomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("String length may not be less than 1");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand());
        }
        return sb.toString();
    }

    public String randomLengthString(int maxlen) {
        return randomString(randBetween(r, 1, maxlen));
    }

    public Set<Character> charset() {
        return new HashSet<>(chars);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        return obj instanceof CharRandom
                && ((CharRandom) obj).chars.equals(chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chars);
    }

    @Override
    public String toString() {
        return "CharRandom (charset: " + (new TreeSet<>(chars)).toString() + ")";
    }

    @Override
    public CharRandom mirror() {
        return new CharRandom(Mirrors.mirror(chars));
    }

    public static Character randStatic() {
        return (new CharRandom()).rand();
    }

    public static Character forCharset(Set<Character> charset) {
        return (new CharRandom(charset)).rand();
    }

    public static Character forCharset(Charsets predefined) {
        return (new CharRandom(predefined)).rand();
    }

    public static Character forCharset(String charset) {
        return (new CharRandom(charset)).rand();
    }

    public static CharRandom fromRandomSubset(Set<Character> superset, int subsetSize) {
        if (subsetSize > superset.size() || subsetSize < 1) {
            throw new IllegalArgumentException("subset may not be bigger than the superset or smaller than 1");
        }

        CharRandom crand = new CharRandom(superset);
        if (subsetSize == superset.size()) {
            return crand;
        }

        Set<Character> subset = new HashSet<>();
        for (int i = 0; i < subsetSize; i++) {
            Character randomSelection = crand.rand();
            while (subset.contains(randomSelection)) {
                randomSelection = crand.rand();
            }
            subset.add(randomSelection);
        }

        return new CharRandom(subset);
    }

    public static CharRandom fromRandomSubset(Charsets superset, int subsetSize) {
        return fromRandomSubset(superset.charset(), subsetSize);
    }

    public static CharRandom fromRandomSubset(String superset, int subsetSize) {
        return fromRandomSubset(Charsets.toSet(superset), subsetSize);
    }

    public static CharRandom fromRandomSubset(Set<Character> superset) {
        return fromRandomSubset(superset, randBetween(1, superset.size()));
    }

    public static CharRandom fromRandomSubset(Charsets superset) {
        return fromRandomSubset(superset.charset());
    }

    public static CharRandom fromRandomSubset(String superset) {
        return fromRandomSubset(Charsets.toSet(superset));
    }

    public static CharRandom fromRandomSubset(int subsetSize) {
        return fromRandomSubset(new HashSet<>(DEFAULT_PATTERN), subsetSize);
    }

    public static CharRandom fromRandomSubset() {
        return fromRandomSubset(new HashSet<>(DEFAULT_PATTERN));
    }
}
