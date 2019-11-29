package com.tara.util.async.tasks.criteria;

public abstract class TaskCriterion {
    protected boolean observed;

    protected boolean requireObservance(boolean given) {
        return observed && given;
    }

    protected TaskCriterion() {
        observed = false;
    }

    public void startObservance() {
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
