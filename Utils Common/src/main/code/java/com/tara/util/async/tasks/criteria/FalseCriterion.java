package com.tara.util.async.tasks.criteria;

public class FalseCriterion extends TaskCriterion {
    private static final boolean FALSE = false;

    public FalseCriterion() {
        super();
    }

    @Override
    public boolean given() {
        return FALSE;
    }

    @Override
    public void reset() {
        // no action needed
    }
}