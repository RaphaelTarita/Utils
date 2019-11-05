package com.tara.util.java.async.tasks.procedure;

@FunctionalInterface
public interface TaskProcedure {
    void execute() throws ProcedureException;
}
