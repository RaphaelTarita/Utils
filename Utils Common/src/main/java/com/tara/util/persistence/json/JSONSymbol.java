package com.tara.util.persistence.json;

public enum JSONSymbol {
    BRACE_OPEN("{"),
    BRACE_CLOSE("}"),
    NULL("null"),
    TRUE("true"),
    FALSE("false"),
    QUOT_MARK("\""),
    BRACKET_OPEN("["),
    BRACKET_CLOSE("]"),
    COLON(":"),
    COMMA(","),

    SPACE(" "),
    LINEFEED(System.lineSeparator());

    private final String symbol;

    JSONSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static JSONSymbol getSymbol(String s) {
        for (JSONSymbol value : values()) {
            if (value.symbol.equals(s)) {
                return value;
            }
        }
        return null;
    }
}
