package com.tara.util.async.tasks.criteria;

public class NegateCriterion extends TaskCriterion {
    private TaskCriterion criterion;

    public NegateCriterion(TaskCriterion criterion) {
        super();
        this.criterion = criterion;
    }

    @Override
    public void startObservance() {
        observed = true;
        criterion.startObservance();
    }

    @Override
    public boolean given() {
        return requireObservance(!criterion.given());
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

    @Override
    public TaskCriterion mirror() {
        return withObservanceState(new NegateCriterion(criterion.mirror()));
    }
}
