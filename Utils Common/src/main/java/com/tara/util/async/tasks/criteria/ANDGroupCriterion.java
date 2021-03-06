package com.tara.util.async.tasks.criteria;

import com.tara.util.mirror.Mirrors;

import java.util.List;

public class ANDGroupCriterion extends GroupCriterion {
    public ANDGroupCriterion(List<TaskCriterion> criteria) {
        super(criteria);
    }

    public ANDGroupCriterion(TaskCriterion criterion) {
        super(criterion);
    }

    public ANDGroupCriterion(TaskCriterion... criteria) {
        super(criteria);
    }

    @Override
    public boolean given() {
        for (TaskCriterion criterion : criteria) {
            if (!criterion.given()) {
                return requireObservance(false);
            }
        }
        return requireObservance(true);
    }

    @Override
    public ANDGroupCriterion mirror() {
        return withObservanceState(
                new ANDGroupCriterion(
                        Mirrors.mirror(
                                criteria
                        )
                )
        );
    }
}
