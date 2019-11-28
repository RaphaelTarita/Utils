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
    public void startObservance() {
        super.startObservance();
        revoke = false;
    }

    @Override
    public boolean given() {
        return observed && !revoke;
    }

    @Override
    public void reset() {
        revoke = false;
    }
}