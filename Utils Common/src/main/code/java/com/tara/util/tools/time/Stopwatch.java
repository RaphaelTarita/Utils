package com.tara.util.tools.time;

import java.time.Duration;

public class Stopwatch {
    public enum Unit {
        NANOS(1, 1.0),
        MICROS(NANOS.time * 1000, NANOS.dTime * 1000.0),
        MILLIS(MICROS.time * 1000, MICROS.dTime * 1000.0),
        SECONDS(MILLIS.time * 1000, MILLIS.dTime * 1000.0),
        MINUTES(SECONDS.time * 60, SECONDS.dTime * 60.0),
        HOURS(MINUTES.time * 60, MINUTES.dTime * 60.0),
        DAYS(HOURS.time * 24, HOURS.dTime * 24.0),
        YEARS(DAYS.time * 365, DAYS.dTime * 365.0);

        private final long time;
        private final double dTime;

        Unit(long time, double dTime) {
            this.time = time;
            this.dTime = dTime;
        }

        public long value() {
            return time;
        }

        public double dValue() {
            return dTime;
        }
    }

    private long start;
    private long current;

    private long current() {
        return start != -1
                ? current += System.nanoTime() - start
                : current;
    }

    public Stopwatch() {
        start = -1;
        current = 0;
    }

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        current();
        start = -1;
    }

    public void reset() {
        current = 0;
        start();
    }

    public Duration readDuration() {
        return Duration.ofNanos(current());
    }

    public long read(Unit unit) {
        return current() / unit.value();
    }

    public double readDouble(Unit unit) {
        return current() / unit.dValue();
    }

    public long readNanos() {
        return read(Unit.NANOS);
    }

    public double readNanosDouble() {
        return readDouble(Unit.NANOS);
    }

    public double readMicros() {
        return read(Unit.MICROS);
    }

    public double readMicrosDouble() {
        return readDouble(Unit.MICROS);
    }

    public long readMillis() {
        return read(Unit.MILLIS);
    }

    public double readMillisDouble() {
        return readDouble(Unit.MILLIS);
    }

    public long readSeconds() {
        return read(Unit.SECONDS);
    }

    public double readSecondsDouble() {
        return readDouble(Unit.SECONDS);
    }

    public long readMinutes() {
        return read(Unit.MINUTES);
    }

    public double readMinutesDouble() {
        return readDouble(Unit.MINUTES);
    }

    public long readHours() {
        return read(Unit.HOURS);
    }

    public double readHoursDouble() {
        return readDouble(Unit.HOURS);
    }

    public long readDays() {
        return read(Unit.DAYS);
    }

    public double readDaysDouble() {
        return readDouble(Unit.DAYS);
    }

    public long readYears() {
        return read(Unit.YEARS);
    }

    public double readYearsDouble() {
        return read(Unit.YEARS);
    }
}
