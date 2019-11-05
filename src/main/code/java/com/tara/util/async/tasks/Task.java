package com.tara.util.async.tasks;

import com.tara.util.async.tasks.criteria.ANDGroupCriterion;
import com.tara.util.async.tasks.criteria.GroupCriterion;
import com.tara.util.async.tasks.criteria.ManualInvokeCriterion;
import com.tara.util.async.tasks.criteria.TaskCriterion;
import com.tara.util.async.tasks.lock.LockingAction;
import com.tara.util.async.tasks.lock.TaskLock;
import com.tara.util.async.tasks.procedure.ProcedureException;
import com.tara.util.async.tasks.procedure.TaskProcedure;
import com.tara.util.async.tasks.procedure.TaskProcedureException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Task implements Runnable {
    private Long iterations;
    private final TaskID id;
    private TaskLock lock;
    private boolean retry;
    private TaskProcedure taskProcedure;
    private TaskCriterion criterion;

    private void resetAll() {
        criterion.reset();
    }

    public Task(String taskName, TaskProcedure taskProcedure, TaskCriterion criterion) {
        id = new TaskID(taskName);
        lock = new TaskLock(id);
        retry = false;
        this.taskProcedure = taskProcedure;
        this.criterion = criterion;
        iterations = 0L;
    }

    public Task(String taskName, TaskProcedure taskProcedure, List<TaskCriterion> criteria) {
        this(
                taskName,
                taskProcedure,
                new ANDGroupCriterion(criteria)
        );
    }

    public Task(String taskName, TaskProcedure taskProcedure, TaskCriterion... criteria) {
        this(
                taskName,
                taskProcedure,
                new ANDGroupCriterion(criteria)
        );
    }

    public void addCriterion(TaskCriterion newCriterion) {
        if (criterion instanceof GroupCriterion) {
            ((GroupCriterion) criterion).add(newCriterion);
        } else if (newCriterion instanceof GroupCriterion) {
            ((GroupCriterion) newCriterion).add(criterion);
            criterion = newCriterion;
        } else {
            criterion = new ANDGroupCriterion(criterion, newCriterion);
        }
        criterion.startObservance();
    }

    public void schedule() {
        criterion.startObservance();
    }

    public boolean ready() {
        return criterion.given();
    }

    public void turnOn() {
        if (criterion instanceof ManualInvokeCriterion) {
            ((ManualInvokeCriterion) criterion).invoke();
        } else if (criterion instanceof GroupCriterion) {
            ((GroupCriterion) criterion).invokeManuals();
        }
    }

    public void execute() {
        if (ready()) {
            if (!locked()) {
                forceExecute();
            } else if (lock.rlocks() && !retry) {
                log.info("Task execution delayed, RLock active: {}", lock);
                retry = true;
                // no reset
            } else {
                log.warn("Cannot execute Task because it is locked: {}", lock);
                resetAll();
            }
        }
    }

    public boolean locked() {
        return lock.locks();
    }

    public boolean rlocked() {
        return lock.rlocks();
    }

    public boolean blocked() {
        return lock.blocks();
    }

    public boolean scheduledForRetry() {
        return retry;
    }

    public void recover() {
        if (lock.blocks()) {
            log.info("Recovering Task {}...", toString());
            try {
                lock.lockOrRenewOnAction(LockingAction.RECOVER);
                taskProcedure.execute();
                log.debug("Successfully recovered");
                lock.lift();
            } catch (ProcedureException ex) {
                log.debug("Recovery failed, Task stays locked");
                lock.renewOnException(new TaskProcedureException(ex, id));
            }
        }
    }

    public void forceExecute() {
        resetAll();
        try {
            lock.lockOrRenewOnAction(LockingAction.EXECUTE);
            taskProcedure.execute();
            lock.liftRLock();
            retry = false;
            iterations++;
        } catch (ProcedureException ex) {
            log.debug("Exception occurred in Task, locking...");
            lock.lockOrRenewOnException(new TaskProcedureException(ex, id));
        }
    }

    public long iterations() {
        return iterations;
    }

    public TaskID id() {
        return id;
    }

    @Override
    public void run() {
        execute();
    }

    @Override
    public String toString() {
        return id.name() + '#' + iterations + " (id: " + id.index() + ')';
    }
}
