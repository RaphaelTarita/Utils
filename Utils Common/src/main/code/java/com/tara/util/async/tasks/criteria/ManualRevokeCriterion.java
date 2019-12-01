package com.tara.util.async.tasks.criteria;

public class ManualRevokeCriterion extends TaskCriterion {
    private boolean revoke;

    public ManualRevokeCriterion() {
        super();
        revoke = false;
    }

    public void revoke() {
        revoke = true;
    }

    @Override
    public boolean given() {
        return requireObservance(!revoke);
    }

    @Override
    public void reset() {
        revoke = false;
    }
}
