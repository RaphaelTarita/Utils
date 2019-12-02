package com.tara.util.async.tasks.criteria;

public class FalseCriterion extends TaskCriterion {
    private static final boolean FALSE = false;

    public FalseCriterion() {
        super();
    }

    @Override
    public boolean given() {
        return requireObservance(FALSE);
    }

    @Override
    public void reset() {
        // no action needed
    }

    @Override
    public TaskCriterion mirror() {
        return withObservanceState(new FalseCriterion());
    }
}
