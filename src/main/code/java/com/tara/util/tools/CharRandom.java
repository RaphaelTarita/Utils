package com.tara.util.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CharRandom {
    private static final String ALPHANUM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String EXTRAS = "_-+#@~*";
    private static final Set<Character> DEFAULT_PATTERN = toSet(ALPHANUM + EXTRAS);

    private Random r;
    private final List<Character> chars;
    private int seedCache;

    private static Set<Character> toSet(String str) {
        return new HashSet<>(
                List.of(
                        str
                                .chars()
                                .mapToObj(c -> (char) c)
                                .toArray(Character[]::new)
                )
        );
    }

    private int charsizeRandom() {
        return r.nextInt(chars.size());
    }

    public CharRandom(Set<Character> charset) {
        seedCache = charset.hashCode();
        r = new Random(seedCache);
        chars = new ArrayList<>(charset);
    }

    public CharRandom(String charset) {
        this(toSet(charset));
    }

    public CharRandom() {
        this(DEFAULT_PATTERN);
    }

    public void changeSeed(int seed) {
        if (seed != seedCache) {
            seedCache = seed;
            r = new Random(seedCache);
        }
    }

    public Character rand() {
        return chars.get(charsizeRandom());
    }

    public Character rand(int seed) {
        changeSeed(seed);
        return rand();
    }

    public String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand());
        }
        return sb.toString();
    }

    public String randomLengthString(int maxlen) {
        return randomString(r.nextInt(maxlen + 1));
    }
}
