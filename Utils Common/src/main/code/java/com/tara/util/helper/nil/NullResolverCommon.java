package com.tara.util.helper.nil;

import java.util.Objects;
import java.util.function.BiPredicate;

public abstract class NullResolverCommon implements NullResolver {
    private static boolean modeNotSupported(EqualsMode mode) {
        throw new UnsupportedOperationException("Null Equals mode \'" + mode.name() + "\' is not supported");
    }

    private final NullResolverImpl impl;

    protected NullResolverCommon(NullResolverImpl impl) {
        this.impl = impl;
    }

    protected boolean equalsArray(BiPredicate<Object, Object> mode, Object[] ops) {
        if (ops.length < 2) {
            return false;
        }
        Object prev = null;
        boolean first = true;
        for (Object op : ops) {
            if (first) {
                prev = op;
                first = false;
                continue;
            }

            if (!mode.test(prev, op)) {
                return false;
            }
            prev = op;
        }
        return true;
    }

    protected abstract boolean equalsNullFalse(Object lop, Object rop);

    protected abstract boolean equalsAllNullTrue(Object lop, Object rop);

    protected abstract boolean equalsOneNullTrue(Object lop, Object rop);

    protected boolean equalsNullFalse(Object[] ops) {
        return equalsArray(this::equalsNullFalse, ops);
    }

    protected boolean equalsAllNullTrue(Object[] ops) {
        return equalsArray(this::equalsAllNullTrue, ops);
    }

    protected boolean equalsOneNullTrue(Object[] ops) {
        return equalsArray(this::equalsOneNullTrue, ops);
    }

    @Override
    public NullResolverImpl impl() {
        return impl;
    }

    @Override
    public boolean nullEquals(EqualsMode mode, Object lop, Object rop) {
        switch (mode) {
            case NULL_FALSE:
                return equalsNullFalse(lop, rop);
            case ALL_NULL_TRUE:
                return equalsAllNullTrue(lop, rop);
            case ONE_NULL_TRUE:
                return equalsOneNullTrue(lop, rop);
            default:
                return modeNotSupported(mode);
        }
    }

    @Override
    public boolean nullEquals(EqualsMode mode, Object... ops) {
        switch (mode) {
            case NULL_FALSE:
                return equalsNullFalse(ops);
            case ALL_NULL_TRUE:
                return equalsAllNullTrue(ops);
            case ONE_NULL_TRUE:
                return equalsOneNullTrue(ops);
            default:
                return modeNotSupported(mode);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BasicNullResolver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impl);
    }
}
