package com.tara.util.persistence.http;

public enum HTTPSymbol {
    SPACE(" "),
    LINEBREAK(System.lineSeparator()),
    SLASH("/"),
    QUOT("?"),
    EQUALS("="),
    ET("&"),
    COLON(":");

    private final String rep;

    HTTPSymbol(String rep) {
        this.rep = rep;
    }

    public String toString() {
        return rep;
    }

    public char getch() {
        return rep.charAt(0);
    }
}
