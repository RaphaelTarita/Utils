package com.tara.util.java.async.tasks.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GroupCriterion extends TaskCriterion {
    private List<TaskCriterion> criteria;
    private boolean checkAll;

    public GroupCriterion(List<TaskCriterion> criteria, boolean checkAll) {
        this.criteria = criteria;
        this.checkAll = checkAll;
    }

    public GroupCriterion(List<TaskCriterion> criteria) {
        this(criteria, true);
    }

    public GroupCriterion(TaskCriterion criterion) {
        this(new ArrayList<>(Collections.singleton(criterion)));
    }

    public GroupCriterion(TaskCriterion... criteria) {
        this(new ArrayList<>(Arrays.asList(criteria)));
    }

    public void add(TaskCriterion criterion) {
        criterion.startObservance();
        criteria.add(criterion);
    }

    @Override
    public void startObservance() {
        super.startObservance();
        criteria.forEach(TaskCriterion::startObservance);
    }

    @Override
    public boolean given() {
        for (TaskCriterion criterion : criteria) {
            boolean given = criterion.given();
            if (checkAll && !given) {
                return false;
            } else if (!checkAll && given) {
                return true;
            }
        }
        return checkAll;
    }

    @Override
    public void reset() {

        criteria.forEach(TaskCriterion::reset);
    }

    public void stopObservance() {
        super.stopObservance();
        criteria.forEach(TaskCriterion::stopObservance);
    }

    public void executeForEach(Consumer<TaskCriterion> criteriaAction) {
        criteria.forEach(criteriaAction);
        criteriaAction.accept(this);
    }

}
