package com.tara.util.async.tasks.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class GroupCriterion extends TaskCriterion {
    protected List<TaskCriterion> criteria;

    protected GroupCriterion(List<TaskCriterion> criteria) {
        super();
        this.criteria = criteria;
    }

    protected GroupCriterion(TaskCriterion criterion) {
        this(new ArrayList<>(Collections.singleton(criterion)));
    }

    protected GroupCriterion(TaskCriterion... criteria) {
        this(new ArrayList<>(Arrays.asList(criteria)));
    }

    public void add(TaskCriterion criterion) {
        if (observed) {
            criterion.startObservance();
        }
        criteria.add(criterion);
    }

    @Override
    public void startObservance() {
        observed = true;
        criteria.forEach(TaskCriterion::startObservance);
    }

    @Override
    public abstract boolean given();

    @Override
    public void reset() {
        criteria.forEach(TaskCriterion::reset);
    }

    @Override
    public void stopObservance() {
        super.stopObservance();
        criteria.forEach(TaskCriterion::stopObservance);
    }

    public void executeForEach(Consumer<TaskCriterion> criteriaAction) {
        criteria.forEach(criteriaAction);
        criteriaAction.accept(this);
    }

    public void invokeManuals() {
        for (TaskCriterion criterion : criteria) {
            if (criterion instanceof ManualInvokeCriterion) {
                ((ManualInvokeCriterion) criterion).invoke();
            } else if (criterion instanceof GroupCriterion) {
                ((GroupCriterion) criterion).invokeManuals();
            }
        }
    }

    public void revokeManuals() {
        for (TaskCriterion criterion : criteria) {
            if (criterion instanceof ManualRevokeCriterion) {
                ((ManualRevokeCriterion) criterion).revoke();
            } else if (criterion instanceof GroupCriterion) {
                ((GroupCriterion) criterion).revokeManuals();
            }
        }
    }
}
