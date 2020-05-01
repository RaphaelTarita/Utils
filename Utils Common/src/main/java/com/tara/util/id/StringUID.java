package com.tara.util.id;

import com.tara.util.mirror.Mirrorable;
import com.tara.util.tools.CharRandom;
import com.tara.util.tools.Charsets;

import java.util.Objects;

public class StringUID implements UID, Mirrorable<StringUID> {
    private static final long serialVersionUID = 9114740774376532987L;
    private static final int DEFAULT_LENGTH = 10;
    private static final Charsets DEFAULT_CHARSET = Charsets.ALPHANUM;

    private final String string;

    static {
        UID.registerBuilder(StringUID.class, StringUID::new);
    }

    public StringUID(String str) {
        string = str;
        registerUID();
    }

    public StringUID(CharRandom randomEngine, int length) {
        String candidate = randomEngine.randomString(length);
        while (!check()) {
            candidate = randomEngine.randomString(length);
        }
        string = candidate;
        registerUID();
    }

    public StringUID(Charsets charset, int length) {
        this(new CharRandom(charset), length);
    }

    public StringUID(CharRandom randomEngine) {
        this(randomEngine, DEFAULT_LENGTH);
    }

    public StringUID(Charsets charset) {
        this(charset, DEFAULT_LENGTH);
    }

    public StringUID(int length) {
        this(DEFAULT_CHARSET, length);
    }

    public StringUID() {
        this(DEFAULT_CHARSET, DEFAULT_LENGTH);
    }

    @Override
    public String mapUID() {
        return string;
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

    @Override
    public StringUID mirror() {
        return new StringUID(string);
    }
}
