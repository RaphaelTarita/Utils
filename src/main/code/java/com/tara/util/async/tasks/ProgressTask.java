package com.tara.util.async.tasks;

import com.tara.util.async.tasks.criteria.ANDGroupCriterion;
import com.tara.util.async.tasks.criteria.TaskCriterion;
import com.tara.util.async.tasks.procedure.ProgressTaskProcedure;
import com.tara.util.async.tasks.procedure.TaskProcedure;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ProgressTask extends Task {
    private final AtomicLong progressGoal;
    private AtomicLong progressSteps;

    public ProgressTask(String taskName, ProgressTaskProcedure taskProcedure, long progressGoal, TaskCriterion criterion) {
        super(taskName, criterion);
        this.progressGoal = new AtomicLong(progressGoal);
        this.progressSteps = new AtomicLong();
        super.setProcedure(composeSimpleProcedure(taskProcedure));
    }

    public ProgressTask(String taskName, ProgressTaskProcedure taskProcedure, long progressGoal, List<TaskCriterion> criteria) {
        this(taskName, taskProcedure, progressGoal, new ANDGroupCriterion(criteria));
    }

    public ProgressTask(String taskName, ProgressTaskProcedure taskProcedure, long progressGoal, TaskCriterion... criteria) {
        this(taskName, taskProcedure, progressGoal, new ANDGroupCriterion(criteria));
    }

    private TaskProcedure composeSimpleProcedure(ProgressTaskProcedure progressProcedure) {
        return () -> progressProcedure.execute(progressSteps);
    }

    public long progress() {
        return progressSteps.get();
    }

    public long goal() {
        return progressGoal.get();
    }

    public double proportionalProgress() {
        long progress = progress();
        return progress == 0
                ? 0
                : ((double) goal()) / progress;
    }
}
