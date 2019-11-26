package com.tara.util.async.tasks.procedure;

public class ProcedureException extends Exception {
    private static final long serialVersionUID = -445405868980110698L;
    private final Exception root;

    public ProcedureException(Exception root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "ProcedureException from plain TaskProcedure: " + root.toString();
    }
}
