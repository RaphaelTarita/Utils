package com.tara.util.async.tasks.procedure;

@FunctionalInterface
public interface TaskProcedure {
    void execute() throws ProcedureException;
}
