package com.tara.util.async.tasks.criteria;

import com.tara.util.mirror.Mirrorable;

public abstract class TaskCriterion implements Mirrorable<TaskCriterion> {
    protected boolean observed;

    protected <C extends TaskCriterion> C withObservanceState(C criterion) {
        criterion.observed = this.observed;
        return criterion;
    }

    protected boolean requireObservance(boolean given) {
        return observed && given;
    }

    protected TaskCriterion() {
        observed = false;
    }

    public void startObservance() {
        reset();
        observed = true;
    }

    public abstract boolean given();

    public abstract void reset();

    public void stopObservance() {
        observed = false;
        reset();
    }

    public NegateCriterion negate() {
        return new NegateCriterion(this);
    }
}
