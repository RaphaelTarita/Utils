package com.tara.util.persistence.http;

public enum HTTPVersion {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2("HTTP/2"),
    HTTP_3("HTTP/3");

    private final String value;

    HTTPVersion(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
