package com.tara.util.java.async.tasks.criteria;

import java.util.Objects;

public class NullWatcherCriterion<W> extends WatcherCriterion<W> {
    protected NullWatcherCriterion(W watch) {
        super(
                watch,
                Objects::nonNull
        );
    }
}
