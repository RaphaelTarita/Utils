package com.tara.util.async.tasks.procedure;

import java.util.concurrent.atomic.AtomicLong;

@FunctionalInterface
public interface ProgressTaskProcedure {
    void execute(AtomicLong progressSteps) throws ProcedureException;
}
