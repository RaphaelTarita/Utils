package com.tara.util.persistence.http;

public enum HTTPHeader {
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    DATE("Date"),
    HOST("Host"),
    KEEP_ALIVE("Keep-Alive");

    private final String value;

    HTTPHeader(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
