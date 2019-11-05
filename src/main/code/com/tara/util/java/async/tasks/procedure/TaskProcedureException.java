package com.tara.util.java.async.tasks.procedure;

import com.tara.util.java.async.tasks.TaskID;

public class TaskProcedureException extends Exception {
    private final ProcedureException plain;
    private final TaskID taskID;

    public TaskProcedureException(ProcedureException plain, TaskID id) {
        this.plain = plain;
        this.taskID = id;
    }

    @Override
    public String toString() {
        return "TaskProcedureException in Task: " + taskID.toString() + ", Plain Cause: " + plain.toString();
    }
}
