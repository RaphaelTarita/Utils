package com.tara.util.async.tasks.criteria;

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
        super.startObservance();
        invoke = false;
    }

    @Override
    public boolean given() {
        return observed && invoke;
    }

    @Override
    public void reset() {
        invoke = false;
    }
}
