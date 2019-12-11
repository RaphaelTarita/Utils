package com.tara.util.persistence.json;

import java.util.Objects;

public class MarshallerFallback {
    private MarshallerFallback() {
    }

    public static StringMarshaller<Object> defaultMarshaller() {
        return Objects::toString;
    }
}
