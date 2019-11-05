package com.tara.util.async.tasks.criteria;

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
                return true;
            }
        }
        return false;
    }
}
