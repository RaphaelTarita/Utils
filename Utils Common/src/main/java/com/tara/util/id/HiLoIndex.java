package com.tara.util.id;

import com.tara.util.mirror.Mirrorable;

import java.util.Objects;

public class HiLoIndex implements UID, Mirrorable<HiLoIndex> {
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

    static {
        UID.registerBuilder(HiLoIndex.class, HiLoIndex::mapString);
    }

    public static HiLoIndex mapString(String strIndex) {
        return new HiLoIndex(Long.parseLong(strIndex));
    }

    public HiLoIndex() {
        index = getNew();
        registerUID();
    }

    public HiLoIndex(long index) {
        this.index = index;
        registerUID();
    }

    public long toLong() {
        return index;
    }

    @Override
    public String mapUID() {
        return String.valueOf(index);
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

    @Override
    public HiLoIndex mirror() {
        return new HiLoIndex(index);
    }
}
