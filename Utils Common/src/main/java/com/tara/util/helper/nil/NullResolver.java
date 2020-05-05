package com.tara.util.helper.nil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class NullResolver {
    private NullResolver() {
    }

    private static boolean modeNotSupported(EqualsMode mode) {
        throw new UnsupportedOperationException("Null Equals mode '" + mode.name() + "' is not supported");
    }

    private static boolean equalsArray(BiPredicate<Object, Object> mode, Object[] ops) {
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

    private static boolean equalsNullFalse(Object lop, Object rop) {
        return (lop != null && rop != null)
            && lop.equals(rop);
    }

    private static boolean equalsAllNullTrue(Object lop, Object rop) {
        return (lop == null && rop == null)
            || (lop != null && lop.equals(rop));
    }

    private static boolean equalsOneNullTrue(Object lop, Object rop) {
        return (lop == null || rop == null)
            || lop.equals(rop);
    }

    public static <N> boolean isNull(N obj) {
        return obj == null;
    }

    public static <N> void resolve(Consumer<N> func, N obj) {
        if (!isNull(obj)) {
            func.accept(obj);
        }
    }

    public static <N, R> R resolve(Function<N, R> func, N obj, R orElse) {
        return isNull(obj)
            ? orElse
            : func.apply(obj);
    }

    public static <N, P> void resolve(BiConsumer<N, P> func, N obj, P param) {
        if (!isNull(obj)) {
            func.accept(obj, param);
        }
    }

    public static <N, P, R> R resolve(BiFunction<N, P, R> func, N obj, P param, R orElse) {
        return isNull(obj)
            ? orElse
            : func.apply(obj, param);
    }

    public static <N, R> R resolve(Function<N, R> func, N obj) {
        return resolve(func, obj, null);
    }

    public static <N, P, R> R resolve(BiFunction<N, P, R> func, N obj, P param) {
        return resolve(func, obj, param, null);
    }

    public static <N> N choose(N obj1, N obj2) {
        return isNull(obj1)
            ? obj2
            : obj1;
    }

    @SafeVarargs
    public static <N> N chooseFrom(N... objs) {
        for (N obj : objs) {
            if (!isNull(obj)) {
                return obj;
            }
        }
        return null;
    }

    @SafeVarargs
    public static <N> List<N> chooseAllFrom(N... objs) {
        List<N> list = new ArrayList<>();
        for (N obj : objs) {
            if (!isNull(obj)) {
                list.add(obj);
            }
        }
        return list;
    }

    public static boolean nullEquals(EqualsMode mode, Object lop, Object rop) {
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

    public static boolean nullEquals(EqualsMode mode, Object... ops) {
        switch (mode) {
            case NULL_FALSE:
                return equalsArray(NullResolver::equalsNullFalse, ops);
            case ALL_NULL_TRUE:
                return equalsArray(NullResolver::equalsAllNullTrue, ops);
            case ONE_NULL_TRUE:
                return equalsArray(NullResolver::equalsOneNullTrue, ops);
            default:
                return modeNotSupported(mode);
        }
    }
}
