package com.tara.util.async.tasks.criteria;

import com.tara.util.mirror.Mirrorable;

public class MirrorChangeWatcherCriterion<W extends Mirrorable<W>> extends ChangeWatcherCriterion<W> {
    public MirrorChangeWatcherCriterion(W watch) {
        super(
                watch,
                Mirrorable::mirror
        );
    }
}
