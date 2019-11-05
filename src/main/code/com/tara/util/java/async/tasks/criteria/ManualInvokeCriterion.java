package com.tara.util.java.async.tasks.criteria;

public class ManualInvokeCriterion extends TaskCriterion {
    private boolean invoke;

    public ManualInvokeCriterion() {
        super();
        invoke = false;
    }

    public void invoke() {
        invoke = true;
    }

    @Override
    public void startObservance() {
        invoke = false;
        observed = true;
    }

    @Override
    public boolean given() {
        if (observed && invoke) {
            invoke = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        invoke = false;
    }
}
