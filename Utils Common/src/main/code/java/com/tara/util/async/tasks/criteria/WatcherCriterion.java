package com.tara.util.async.tasks.criteria;

import com.tara.util.mirror.Mirrors;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class WatcherCriterion<W> extends TaskCriterion {
    private AtomicReference<W> watch;
    private Predicate<W> watcher;

    protected WatcherCriterion(W watch) {
        this(watch, null);
    }

    protected W getWatch() {
        return watch.get();
    }

    protected void setWatch(W newWatch) {
        watch = new AtomicReference<>(newWatch);
    }

    protected void assignWatcher(Predicate<W> watcher) {
        this.watcher = watcher;
    }

    public WatcherCriterion(W watch, Predicate<W> watcher) {
        super();
        this.watch = new AtomicReference<>(watch);
        this.watcher = watcher;
    }

    @Override
    public boolean given() {
        return requireObservance(watcher.test(watch.get()));
    }

    @Override
    public void reset() {
        // no action needed
    }

    @Override
    public WatcherCriterion<W> mirror() {
        return withObservanceState(
                new WatcherCriterion<>(
                        Mirrors.mirror(
                                watch.get()
                        ),
                        watcher
                )
        );
    }
}
