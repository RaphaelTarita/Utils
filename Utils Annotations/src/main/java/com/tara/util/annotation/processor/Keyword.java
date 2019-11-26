package com.tara.util.annotation.processor;

public enum Keyword {
    ABSTRACT("abstract"),
    ASSERT("assert"),
    BOOLEAN("boolean"),
    BREAK("break"),
    BYTE("byte"),
    CASE("case"),
    CATCH("catch"),
    CHAR("char"),
    CLASS("class"),
    CONTINUE("continue"),
    DEFAULT("default"),
    DO("do"),
    DOUBLE("double"),
    ELSE("else"),
    ENUM("enum"),
    EXPORTS("exports"),
    EXTENDS("extends"),
    FINAL("final"),
    FINALLY("finally"),
    FLOAT("float"),
    FOR("for"),
    IF("if"),
    IMPLEMENTS("implements"),
    IMPORT("import"),
    INSTANCEOF("instanceof"),
    INT("int"),
    INTERFACE("interface"),
    LONG("long"),
    MODULE("module"),
    NATIVE("native"),
    NEW("new"),
    PACKAGE("package"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public"),
    REQUIRES("requires"),
    RETURN("return"),
    SHORT("short"),
    STATIC("static"),
    STRICTFP("strictfp"),
    SUPER("super"),
    SWITCH("switch"),
    SYNCHRONIZED("synchronized"),
    THIS("this"),
    THROW("throw"),
    THROWS("throws"),
    TRANSIENT("transient"),
    TRY("try"),
    VOID("void"),
    VOLATILE("volatile"),
    WHILE("while"),

    TRUE("true"),
    NULL("null"),
    FALSE("false"),

    VAR("var"),

    CONST("const"),
    GOTO("goto");

    private final String word;

    Keyword(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

    public Keyword isKeyword(String str) {
        for (Keyword k : values()) {
            if (k.word.equals(str)) {
                return k;
            }
        }
        return null;
    }
}
