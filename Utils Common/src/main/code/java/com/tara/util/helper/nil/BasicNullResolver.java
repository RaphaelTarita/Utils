package com.tara.util.helper.nil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicNullResolver extends NullResolverCommon {
    private static final BasicNullResolver instance = new BasicNullResolver();

    public static BasicNullResolver instance() {
        return instance;
    }

    private BasicNullResolver() {
        super(NullResolverImpl.BASIC);
    }

    @Override
    protected boolean equalsNullFalse(Object lop, Object rop) {
        return (lop != null && rop != null)
                && lop.equals(rop);
    }

    @Override
    protected boolean equalsAllNullTrue(Object lop, Object rop) {
        return (lop == null && rop == null)
                || (lop != null && lop.equals(rop));
    }

    @Override
    protected boolean equalsOneNullTrue(Object lop, Object rop) {
        return (lop == null || rop == null)
                || lop.equals(rop);
    }

    @Override
    public <N> boolean isNull(N obj) {
        return obj == null;
    }

    @Override
    public <N> void resolve(Consumer<N> func, N obj) {
        if (obj != null) {
            func.accept(obj);
        }
    }

    @Override
    public <N, R> R resolve(Function<N, R> func, N obj, R orElse) {
        return obj != null
                ? func.apply(obj)
                : orElse;
    }

    @Override
    public <N, P> void resolve(BiConsumer<N, P> func, N obj, P param) {
        if (obj != null) {
            func.accept(obj, param);
        }
    }

    @Override
    public <N, P, R> R resolve(BiFunction<N, P, R> func, N obj, P param, R orElse) {
        return obj != null
                ? func.apply(obj, param)
                : orElse;
    }

    @Override
    public <N> N choose(N obj1, N obj2) {
        return obj1 == null
                ? obj2
                : obj1;
    }

    @Override
    public <N> N chooseFrom(N[] objs) {
        for (N obj : objs) {
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public <N> List<N> chooseAllFrom(N[] objs) {
        List<N> list = new ArrayList<>();
        for (N obj : objs) {
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
