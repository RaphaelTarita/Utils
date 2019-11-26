package com.tara.util.async.tasks.criteria;

public class BoolWatcherCriterion extends WatcherCriterion<Boolean> {

    protected BoolWatcherCriterion(Boolean watch) {
        super(
                watch,
                watchParam -> watchParam
        );
    }
}
