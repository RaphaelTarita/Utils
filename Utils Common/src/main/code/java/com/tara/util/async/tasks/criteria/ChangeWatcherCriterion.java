package com.tara.util.async.tasks.criteria;

import com.tara.util.mirror.Mirrors;

import java.util.function.UnaryOperator;

public class ChangeWatcherCriterion<W> extends WatcherCriterion<W> {
    private W original;
    private UnaryOperator<W> copySupplier;

    public ChangeWatcherCriterion(W watch, UnaryOperator<W> copySupplier) {
        super(watch);
        original = null;
        this.copySupplier = copySupplier;
        assignWatcher(watchParam -> !watchParam.equals(original));
    }

    @Override
    public void reset() {
        original = copySupplier.apply(getWatch());
    }

    @Override
    public ChangeWatcherCriterion<W> mirror() {
        WatcherCriterion<W> superCrit = super.mirror();
        ChangeWatcherCriterion<W> crit = new ChangeWatcherCriterion<>(superCrit.getWatch(), copySupplier);
        crit.original = Mirrors.mirror(this.original);
        return withObservanceState(crit);
    }
}
