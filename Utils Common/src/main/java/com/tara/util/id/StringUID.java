package com.tara.util.id;

import com.tara.util.tools.CharRandom;

import java.util.Objects;
import java.util.function.Function;

public class StringUID implements UID {
    private static final long serialVersionUID = 9114740774376532987L;
    private static final int MAX_RANDOM_ID_LENGTH = 50;

    private static final CharRandom random = new CharRandom();

    private String string;

    public StringUID(String str) {
        string = str;
        register();
    }

    public StringUID() {
        string = random.randomLengthString(MAX_RANDOM_ID_LENGTH);
        while (!check()) {
            string = random.randomLengthString(MAX_RANDOM_ID_LENGTH);
        }
        register();
    }

    @Override
    public Function<String, UID> stringConverter() {
        return StringUID::new;
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
        return string;
    }
}
