package com.tara.util.java.async.tasks.procedure;

public class ProcedureException extends Exception {
    private final Exception root;

    public ProcedureException(Exception root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "ProcedureException from plain TaskProcedure: " + root.toString();
    }
}
