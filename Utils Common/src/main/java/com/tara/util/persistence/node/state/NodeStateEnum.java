package com.tara.util.persistence.node.state;

public enum NodeStateEnum {
    UNKNOWN("unknown"),
    DETACHED("detached"),
    SYNC("sync"),
    LOCAL("local"),
    REMOTE("remote"),
    CONFLICT("conflict");

    private static int maxchars() {
        int max = 0;
        for (NodeStateEnum state : NodeStateEnum.values()) {
            if (state.str.length() > max) {
                max = state.str.length();
            }
        }
        return max;
    }

    private static final int MAXCHARS = maxchars();
    private final String str;

    NodeStateEnum(String str) {
        this.str = str;
    }

    public String padded() {
        int padding = MAXCHARS - str.length() + 1;
        StringBuilder res = new StringBuilder(str);
        res.append(" ".repeat(Math.max(0, padding)));

        return res.toString();
    }

    public String str() {
        return str;
    }
}
