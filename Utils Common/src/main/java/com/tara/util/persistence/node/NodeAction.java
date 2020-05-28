package com.tara.util.persistence.node;

public enum NodeAction {
    CHECKOUT("checkout"),
    COMMIT("commit"),
    FETCH("fetch"),
    PUSH("push"),
    OTHER("other");

    private final String verb;

    private String alt;

    NodeAction(String verb) {
        this.verb = verb;
        this.alt = "";
    }

    public static NodeAction other(String alt) {
        return OTHER.setAlt(alt);
    }

    public NodeAction setAlt(String alt) {
        this.alt = alt;
        return this;
    }

    @Override
    public String toString() {
        return !alt.isEmpty()
            ? alt
            : verb;
    }
}