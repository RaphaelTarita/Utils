package com.tara.util.java.async.tasks.lock;

public enum LockingAction {
    EXECUTE("execute"),
    RECOVER("recover");

    private final String str;

    LockingAction(String str) {
        this.str = str;
    }

    public String string() {
        return str;
    }
}
