package com.tara.util.persistence.node.state;

public enum NodeStateEnum {
    EMPTY("empty"),
    LOCAL("local"),
    REMOTE("remote"),
    SYNC("sync"),
    ERROR("error");

    private final String rep;

    NodeStateEnum(String rep) {
        this.rep = rep;
    }

    @Override
    public String toString() {
        return rep;
    }
}
