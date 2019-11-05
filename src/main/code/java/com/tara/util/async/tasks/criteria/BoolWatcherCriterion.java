package com.tara.util.async.tasks.criteria;

import java.util.concurrent.atomic.AtomicBoolean;

public class BoolWatcherCriterion extends WatcherCriterion<AtomicBoolean> {

    protected BoolWatcherCriterion(Boolean watch) {
        super(
                new AtomicBoolean(watch),
                AtomicBoolean::get
        );
    }
}
