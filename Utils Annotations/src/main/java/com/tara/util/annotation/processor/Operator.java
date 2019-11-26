package com.tara.util.annotation.processor;

public enum Operator {
    ASSIGNMENT("="),
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    REMAINDER("%"),
    INCREMENT("++"),
    DECREMENT("--"),
    COMPLEMENT("!"),
    EQUALITY("=="),
    INEQUALITY("!="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),
    CONDITIONAL_AND("&&"),
    CONDITIONAL_OR("||"),
    TERNARY_FIRST("?"),
    TERNARY_SECOND(":"),
    TERNARY(TERNARY_FIRST.symbol + TERNARY_SECOND.symbol),
    INSTANCEOF("instanceof"),
    BITWISE_COMPLEMENT("~"),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    UNSIGNED_RIGHT_SHIFT(">>>"),
    AND("&"),
    OR("|"),
    XOR("^"),

    PERIOD("."),
    COMMA(","),
    SEMICOLON(";"),

    PARENTHESIS_OPEN("("),
    PARENTHESIS_CLOSE(")"),
    BRACKET_OPEN("["),
    BRACKET_CLOSE("]"),
    BRACE_OPEN("{"),
    BRACE_CLOSE("}"),
    POINTY_OPEN("<"),
    POINTY_CLOSE(">");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public Operator isOperator(String str) {
        for (Operator o : values()) {
            if (o.symbol.equals(str)) {
                return o;
            }
        }

        return null;
    }
}
