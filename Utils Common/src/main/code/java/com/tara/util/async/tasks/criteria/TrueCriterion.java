package com.tara.util.async.tasks.criteria;

public class TrueCriterion extends TaskCriterion {
    private static final boolean TRUE = true;

    public TrueCriterion() {
        super();
    }

    @Override
    public boolean given() {
        return requireObservance(TRUE);
    }

    @Override
    public void reset() {
        // no action needed
    }
}
