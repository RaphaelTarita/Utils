package com.tara.util.async.tasks.criteria;

import com.tara.util.async.tasks.Task;

public class ChainCriterion extends TaskCriterion {
    private Task preTask;
    private long iterations;
    private long start;

    public ChainCriterion(Task preTask, long iterations) {
        super();
        this.preTask = preTask;
        this.iterations = iterations;
        start = 0;
        observed = false;
    }

    public ChainCriterion(Task preTask) {
        this(preTask, 1);
    }

    @Override
    public void startObservance() {
        super.startObservance();
        start = preTask.iterations();
    }

    @Override
    public boolean given() {
        return requireObservance(iterations <= preTask.iterations() - start);
    }

    @Override
    public void reset() {
        start = preTask.iterations();
    }
}
