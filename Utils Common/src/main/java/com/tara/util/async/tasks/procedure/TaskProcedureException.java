package com.tara.util.async.tasks.procedure;

import com.tara.util.id.TaskID;

public class TaskProcedureException extends Exception {
    private static final long serialVersionUID = -3254152062372880602L;
    private final Exception plain;
    private final TaskID taskID;

    public TaskProcedureException(Exception plain, TaskID id) {
        this.plain = plain;
        this.taskID = id;
    }

    @Override
    public String toString() {
        return "TaskProcedureException in Task: " + taskID.toString() + ", Plain Cause: " + plain.toString();
    }
}
