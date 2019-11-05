package com.tara.util.java.async.tasks.criteria;

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
                return false;
            }
        }
        return true;
    }
}
