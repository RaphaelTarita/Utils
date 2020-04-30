package com.tara.util.persistence.http;

public enum HTTPVersion {
    HTTP_0_9("HTTP/0.9", 900),
    HTTP_1_0("HTTP/1.0", 1000),
    HTTP_1_1("HTTP/1.1", 1100),
    HTTP_2("HTTP/2", 2000),
    HTTP_3("HTTP/3", 3000);

    private final String value;
    private final int order;

    HTTPVersion(String value, int order) {
        this.value = value;
        this.order = order;
    }

    public int order() {
        return order;
    }

    @Override
    public String toString() {
        return value;
    }
}
