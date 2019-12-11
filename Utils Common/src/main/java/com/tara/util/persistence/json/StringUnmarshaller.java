package com.tara.util.persistence.json;

import java.text.ParseException;

@FunctionalInterface
public interface StringUnmarshaller<R> {
    R unmarshal(String s) throws ParseException;

    static StringUnmarshaller<String> identity() {
        return s -> s;
    }
}
