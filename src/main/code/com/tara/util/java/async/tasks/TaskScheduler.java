package com.tara.util.java.async.tasks;

import com.tara.util.java.async.tasks.criteria.TaskCriterion;
import com.tara.util.java.async.tasks.criteria.TimeCriterion;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TaskScheduler implements Runnable {
    private List<Task> tasks;
    private long updateCycle;
    private Task recoveryTask;
    private Task retryTask;
    private TaskCriterion runCriterion;
    private long startTime;

    private void recoveryTaskImpl() {
        for (Task task : tasks) {
            task.recover();
        }
    }

    private void retryTaskImpl() {
        for (Task task : tasks) {
            if (task.scheduledForRetry()) {
                log.info("retrying task {}...", task);
                if (task.rlocked()) {
                    log.info("Task is still rlocked, retry after interval");
                } else {
                    task.execute();
                }
            }
        }
    }

    private void scheduleAll() {
        for (Task task : tasks) {
            task.schedule();
        }
        recoveryTask.schedule();
        retryTask.schedule();
        runCriterion.startObservance();
    }

    public TaskScheduler(List<Task> tasks, TaskCriterion runCriterion, long updateCycle, long recoverAfter, long retryAfter) {
        this.runCriterion = runCriterion;
        this.updateCycle = updateCycle;
        this.tasks = tasks;
        startTime = 0;

        recoveryTask = new Task(
                "RecoveryTask",
                this::recoveryTaskImpl,
                new TimeCriterion(
                        recoverAfter
                )
        );

        retryTask = new Task(
                "RetryTask",
                this::retryTaskImpl,
                new TimeCriterion(
                        retryAfter
                )
        );
    }

    public TaskScheduler(List<Task> tasks, TaskCriterion runCriterion, long updateCycle) {
        this(tasks, runCriterion, updateCycle, -1, -1);
    }

    public TaskScheduler(List<Task> tasks, TaskCriterion runCriterion) {
        this(tasks, runCriterion, 100);
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        log.info("TaskScheduler thread started");
        scheduleAll();
        try {
            while (runCriterion.given()) {
                Thread.sleep(updateCycle);
                for (Task task : tasks) {
                    if (!task.scheduledForRetry()) {
                        Thread taskThread = new Thread(task, task.toString());
                        taskThread.start();
                    }
                }
                Thread recovery = new Thread(recoveryTask, recoveryTask.toString());
                Thread retry = new Thread(retryTask, retryTask.toString());
                recovery.start();
                retry.start();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            runCriterion.stopObservance();
            log.info("TaskScheduler thread stopped after {} seconds.", (System.currentTimeMillis() - startTime) / 1000);
            startTime = 0;
        }
    }
}
