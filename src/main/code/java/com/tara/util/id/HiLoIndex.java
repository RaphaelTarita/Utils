package com.tara.util.id;

import java.util.Objects;

public class HiLoIndex implements UID<Long> {
    private static final long serialVersionUID = 62459995810455813L;

    private enum HiLo {
        HI,
        LO
    }

    private static long hiIndex = Long.MAX_VALUE;
    private static long loIndex = Long.MIN_VALUE;
    private static HiLo state = HiLo.HI;

    private static void stateSwitch() {
        state = (
                state == HiLo.LO
                        ? HiLo.HI
                        : HiLo.LO
        );
    }

    public static void reset() {
        hiIndex = Long.MAX_VALUE;
        loIndex = Long.MIN_VALUE;
        state = HiLo.HI;
    }

    public static long getNew() {
        stateSwitch();
        switch (state) {
            case HI:
                return hiIndex--;
            case LO:
                return loIndex++;
            default:
                return 0;
        }
    }

    private final long index;

    public HiLoIndex() {
        index = getNew();
    }

    public HiLoIndex(long index) {
        this.index = index;
    }

    public long toLong() {
        return index;
    }

    @Override
    public boolean taken(Long idGenerator) {
        return idGenerator > loIndex && idGenerator < hiIndex;
    }

    @Override
    public UID<Long> newUnique(Long generator) {
        if (taken(generator)) {
            return new HiLoIndex();
        } else {
            return new HiLoIndex(generator);
        }
    }

    @Override
    public Long getGenerator() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof HiLoIndex)
                && (this.index == ((HiLoIndex) obj).index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return String.valueOf(index);
    }
}
