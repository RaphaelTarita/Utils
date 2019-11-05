package com.tara.util.java.async.tasks;

@FunctionalInterface
public interface Procedure {
    //TODO implement ProcedureException
    void execute() throws Exception;
}
