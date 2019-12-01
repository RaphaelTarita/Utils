package com.tara.util.async.tasks.criteria;

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
}
