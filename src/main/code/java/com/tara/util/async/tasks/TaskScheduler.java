package com.tara.util.async.tasks;

import com.tara.util.async.tasks.criteria.TimeCriterion;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TaskScheduler implements Runnable {
    private List<Task> tasks;
    private SchedulerConfig config;
    private Task recoveryTask;
    private Task retryTask;
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
        config.runCriterion().startObservance();
    }

    public TaskScheduler(List<Task> tasks, SchedulerConfig config) {
        this.config = config;
        this.tasks = tasks;
        startTime = 0;

        recoveryTask = new Task(
                "RecoveryTask",
                this::recoveryTaskImpl,
                new TimeCriterion(
                        config.recoverCycle()
                )
        );

        retryTask = new Task(
                "RetryTask",
                this::retryTaskImpl,
                new TimeCriterion(
                        config.retryCycle()
                )
        );
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        log.info("TaskScheduler thread started");
        scheduleAll();
        try {
            while (config.runCriterion().given()) {
                Thread.sleep(config.updateCycle());
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
            config.runCriterion().stopObservance();
            log.info("TaskScheduler thread stopped after {} seconds.", (System.currentTimeMillis() - startTime) / 1000);
            startTime = 0;
        }
    }

    public void start() {
        Thread taskSchedulerThread = new Thread(this, config.threadName());
        taskSchedulerThread.start();
    }
}
