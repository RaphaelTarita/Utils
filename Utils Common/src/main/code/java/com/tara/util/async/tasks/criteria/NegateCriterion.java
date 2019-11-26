package com.tara.util.async.tasks.criteria;

public class NegateCriterion extends TaskCriterion {
    private TaskCriterion criterion;

    public NegateCriterion(TaskCriterion criterion) {
        this.criterion = criterion;
    }

    @Override
    public void startObservance() {
        super.startObservance();
        criterion.startObservance();
    }

    @Override
    public boolean given() {
        return !criterion.given();
    }

    @Override
    public void reset() {
        criterion.reset();
    }

    @Override
    public void stopObservance() {
        super.stopObservance();
        criterion.stopObservance();
    }
}
