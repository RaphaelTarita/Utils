package com.tara.util.helper.nil;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface NullResolver {
    NullResolverImpl impl();
    <N> boolean isNull(N obj);
    <N> void resolve(Consumer<N> func, N obj);
    <N, R> R resolve(Function<N, R> func, N obj, R orElse);
    <N, P> void resolve(BiConsumer<N, P> func, N obj, P param);
    <N, P, R> R resolve(BiFunction<N, P, R> func, N obj, P param, R orElse);
    <N> N choose(N obj1, N obj2);
    <N> N chooseFrom(N[] objs);
    <N> List<N> chooseAllFrom(N[] objs);
    boolean nullEquals(EqualsMode mode, Object lop, Object rop);
    boolean nullEquals(EqualsMode mode, Object... ops);

    @SafeVarargs
    static <N> N choose(NullResolver resolver, N... objs) {
        return resolver.chooseFrom(objs);
    }

    @SafeVarargs
    static <N> List<N> chooseAll(NullResolver resolver, N... objs) {
        return resolver.chooseAllFrom(objs);
    }

    default <N, R> R resolve(Function<N, R> func, N obj) {
        return resolve(func, obj, null);
    }

    default <N, P, R> R resolve(BiFunction<N, P, R> func, N obj, P param) {
        return resolve(func, obj, param, null);
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
