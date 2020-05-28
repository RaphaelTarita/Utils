package com.tara.util.persistence.http.response;

public enum ResponseType {
    INFORMATION(100),
    SUCCESSFUL(200),
    REDIRECTION(300),
    CLIENT_ERROR(400),
    SERVER_ERROR(500);

    private final int range;

    ResponseType(int range) {
        this.range = range;
    }

    public static ResponseType of(int range) {
        for (ResponseType type : values()) {
            if (type.range == range) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid HTTP response code range: " + range);
    }

    public boolean successful() {
        return range == 100 || range == 200;
    }

    public int range() {
        return range;
    }
}