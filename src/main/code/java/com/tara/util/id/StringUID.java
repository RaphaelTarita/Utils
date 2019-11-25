package com.tara.util.id;

import com.tara.util.tools.CharRandom;

import java.util.HashSet;
import java.util.Objects;

public class StringUID implements UID<String> {
    private static final long serialVersionUID = 9114740774376532987L;
    private static final int MAX_RANDOM_ID_LENGTH = 50;

    private static HashSet<String> takenStrings = new HashSet<>();
    private static final CharRandom random = new CharRandom();

    private String string;

    private void register(String str) {
        Objects.requireNonNull(str);
        if (!taken(str)) {
            string = str;
            takenStrings.add(string);
        } else {
            throw new IllegalArgumentException("StringUID " + str + " is taken");
        }
    }

    public StringUID(String str) {
        register(str);
    }

    public StringUID() {
        String str = random.randomString(MAX_RANDOM_ID_LENGTH);
        while (taken(str)) {
            str = random.randomString(MAX_RANDOM_ID_LENGTH);
        }
        register(str);
    }

    @Override
    public boolean taken(String idGenerator) {
        return takenStrings.contains(idGenerator);
    }

    @Override
    public UID<String> newUnique(String generator) {
        return taken(generator)
                ? new StringUID()
                : new StringUID(generator);
    }

    @Override
    public String getGenerator() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        return (obj instanceof StringUID)
                && ((StringUID) obj).string.equals(string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    @Override
    public String toString() {
        return "StringUID{ " + string + " }";
    }
}
