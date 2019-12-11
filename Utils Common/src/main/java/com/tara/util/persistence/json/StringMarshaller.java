package com.tara.util.persistence.json;

@FunctionalInterface
public interface StringMarshaller<P> {
    String marshal(P obj);

    static StringMarshaller<String> identity() {
        return s -> s;
    }
}
