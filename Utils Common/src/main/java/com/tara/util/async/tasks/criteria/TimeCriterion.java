package com.tara.util.async.tasks.criteria;

public class TimeCriterion extends TaskCriterion {
    private long millis;
    private long start;

    public TimeCriterion(long afterMillis) {
        super();
        millis = afterMillis;
        start = 0;
    }

    @Override
    public boolean given() {
        if (millis == -1) {
            return false;
        }
        return requireObservance(millis < System.currentTimeMillis() - start);
    }

    @Override
    public void reset() {
        start = System.currentTimeMillis();
    }

    @Override
    public TimeCriterion mirror() {
        TimeCriterion crit = new TimeCriterion(millis);
        crit.start = this.start;
        return withObservanceState(crit);
    }
}
