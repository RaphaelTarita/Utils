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
    public boolean given() {
        return requireObservance(invoke);
    }

    @Override
    public void reset() {
        invoke = false;
    }
}
