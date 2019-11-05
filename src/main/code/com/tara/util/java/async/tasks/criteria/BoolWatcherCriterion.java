package com.tara.util.java.async.tasks.criteria;

public class BoolWatcherCriterion extends WatcherCriterion<Boolean> {
    public BoolWatcherCriterion(Boolean watch) {
        super(watch, (watchParam) -> watchParam);
    }
}
