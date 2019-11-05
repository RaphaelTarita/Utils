package com.tara.util.java.async.tasks;

import com.tara.util.java.async.tasks.criteria.GroupCriterion;
import com.tara.util.java.async.tasks.criteria.ManualInvokeCriterion;
import com.tara.util.java.async.tasks.criteria.TaskCriterion;
import com.tara.util.java.async.tasks.lock.LockingAction;
import com.tara.util.java.async.tasks.lock.TaskLock;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Task implements Runnable {
    private Long iterations;
    private final TaskID id;
    private TaskLock lock;
    private boolean retry;
    private Procedure task;
    private TaskCriterion criterion;

    private void resetAll() {
        criterion.reset();
    }

    public Task(String taskName, Procedure task, TaskCriterion criterion) {
        id = new TaskID(taskName);
        lock = new TaskLock(id);
        retry = false;
        this.task = task;
        this.criterion = criterion;
        iterations = 0L;
    }

    public Task(String taskName, Procedure task, List<TaskCriterion> criteria) {
        this(
                taskName,
                task,
                new GroupCriterion(criteria)
        );
    }

    public Task(String taskName, Procedure task, TaskCriterion... criteria) {
        this(
                taskName,
                task,
                new GroupCriterion(criteria)
        );
    }

    public void addCriterion(TaskCriterion newCriterion) {
        if (criterion instanceof GroupCriterion) {
            ((GroupCriterion) criterion).add(newCriterion);
        } else if (newCriterion instanceof GroupCriterion) {
            ((GroupCriterion) newCriterion).add(criterion);
            criterion = newCriterion;
        } else {
            criterion = new GroupCriterion(criterion, newCriterion);
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
            ((GroupCriterion) criterion).executeForEach(
                    (partialCriterion) -> {
                        if (partialCriterion instanceof ManualInvokeCriterion) {
                            ((ManualInvokeCriterion) partialCriterion).invoke();
                        }
                    }
            );
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
                task.execute();
                log.debug("Successfully recovered");
                lock.lift();
            } catch (Exception ex) {
                log.debug("Recovery failed, Task stays locked");
                lock.renewOnException(ex);
            }
        }
    }

    public void forceExecute() {
        resetAll();
        try {
            lock.lockOrRenewOnAction(LockingAction.EXECUTE);
            task.execute();
            lock.liftRLock();
            retry = false;
            iterations++;
        } catch (Exception ex) {
            log.debug("Exception occurred in Task, locking...");
            lock.lockOrRenewOnException(ex);
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
