package com.tara.util.async.tasks.criteria;

import com.tara.util.mirror.Mirrors;

import java.util.List;

public class ORGroupCriterion extends GroupCriterion {
    public ORGroupCriterion(List<TaskCriterion> criteria) {
        super(criteria);
    }

    public ORGroupCriterion(TaskCriterion criterion) {
        super(criterion);
    }

    public ORGroupCriterion(TaskCriterion... criteria) {
        super(criteria);
    }

    @Override
    public boolean given() {
        for (TaskCriterion criterion : criteria) {
            if (criterion.given()) {
                return requireObservance(true);
            }
        }
        return requireObservance(false);
    }

    @Override
    public ORGroupCriterion mirror() {
        return withObservanceState(
                new ORGroupCriterion(
                        Mirrors.mirror(criteria)
                )
        );
    }
}
