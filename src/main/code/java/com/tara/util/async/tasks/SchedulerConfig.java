package com.tara.util.async.tasks;

import com.tara.util.async.tasks.criteria.TaskCriterion;
import com.tara.util.async.tasks.criteria.TrueCriterion;

public class SchedulerConfig {
    private final TaskCriterion runCriterion;
    private final long updateCycle;
    private final long recoverCycle;
    private final long retryCycle;
    private final String threadName;

    private SchedulerConfig(TaskCriterion runCriterion, long updateCycle, long recoverCycle, long retryCycle, String threadName) {
        this.runCriterion = runCriterion;
        this.updateCycle = updateCycle;
        this.recoverCycle = recoverCycle;
        this.retryCycle = retryCycle;
        this.threadName = threadName;
    }

    public TaskCriterion runCriterion() {
        return runCriterion;
    }

    public long updateCycle() {
        return updateCycle;
    }

    public long recoverCycle() {
        return recoverCycle;
    }

    public long retryCycle() {
        return retryCycle;
    }

    public String threadName() {
        return threadName;
    }

    public static class Builder {
        private TaskCriterion builderRunCriterion;
        private long builderUpdateCycle;
        private long builderRecoverCyle;
        private long builderRetryCycle;
        private String builderThreadName;

        private Builder() {
            builderRunCriterion = new TrueCriterion();
            builderUpdateCycle = 100;
            builderRecoverCyle = -1;
            builderRetryCycle = -1;
            builderThreadName = "TaskSchedulerMain";
        }

        private Builder(SchedulerConfig config) {
            builderRunCriterion = config.runCriterion;
            builderUpdateCycle = config.updateCycle;
            builderRecoverCyle = config.recoverCycle;
            builderRetryCycle = config.retryCycle;
            builderThreadName = config.threadName;
        }

        public Builder withRunCriterion(TaskCriterion runCriterion) {
            builderRunCriterion = runCriterion;
            return this;
        }

        public Builder withUpdateCycle(long updateCycle) {
            builderUpdateCycle = updateCycle;
            return this;
        }

        public Builder withRecoverCycle(long recoverCycle) {
            builderRecoverCyle = recoverCycle;
            return this;
        }

        public Builder withRetryCycle(long retryCycle) {
            builderRetryCycle = retryCycle;
            return this;
        }

        public Builder withThreadName(String threadName) {
            builderThreadName = threadName;
            return this;
        }

        public SchedulerConfig build() {
            return new SchedulerConfig(
                    builderRunCriterion,
                    builderUpdateCycle,
                    builderRecoverCyle,
                    builderRetryCycle,
                    builderThreadName
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder thisBuilder() {
        return new Builder(this);
    }
}
