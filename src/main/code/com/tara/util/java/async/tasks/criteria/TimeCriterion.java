package com.tara.util.java.async.tasks.criteria;

public class TimeCriterion extends TaskCriterion {
    private long millis;
    private long start;

    public TimeCriterion(long afterMillis) {
        super();
        millis = afterMillis;
        start = 0;
    }

    @Override
    public void startObservance() {
        observed = true;
        start = System.currentTimeMillis();
    }

    @Override
    public boolean given() {
        if (millis == -1) {
            return false;
        }
        return observed && millis < System.currentTimeMillis() - start;
    }

    @Override
    public void reset() {
        start = System.currentTimeMillis();
    }
}
