package com.tara.util.async.tasks.criteria;

import java.util.function.Predicate;

public class WatcherCriterion<W> extends TaskCriterion {
    private W watch;
    private Predicate<W> watcher;

    protected WatcherCriterion(W watch) {
        this(watch, null);
    }

    protected W getWatch() {
        return watch;
    }

    protected void setWatch(W newWatch) {
        watch = newWatch;
    }

    protected void assignWatcher(Predicate<W> watcher) {
        this.watcher = watcher;
    }

    public WatcherCriterion(W watch, Predicate<W> watcher) {
        super();
        this.watch = watch;
        this.watcher = watcher;
    }

    @Override
    public void startObservance() {
        observed = true;
    }

    @Override
    public boolean given() {
        return watcher.test(watch);
    }

    @Override
    public void reset() {
        // no action needed
    }
}
