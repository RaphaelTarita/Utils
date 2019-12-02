package com.tara.util.helper.nil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class InternalNullResolverOptional extends NullResolverCommon implements NullResolver {

    public InternalNullResolverOptional() {
        super(NullResolverImpl.OPTIONAL);
    }

    private <N> Optional<N> optional(N nullable) {
        return Optional.ofNullable(nullable);
    }

    private <N> N chooseFrom0(List<N> from) {
        return optional(from.get(0)).orElseGet(
                () -> {
                    from.remove(0);
                    return chooseFrom0(from);
                }
        );
    }

    @Override
    protected boolean equalsNullFalse(Object lop, Object rop) {
        return optional(lop).equals(optional(rop));
    }

    @Override
    protected boolean equalsAllNullTrue(Object lop, Object rop) {
        return optional(lop).map(
                objectL -> optional(rop).map(
                        objectL::equals
                ).orElse(false)
        ).orElse(false);
    }

    @Override
    protected boolean equalsOneNullTrue(Object lop, Object rop) {
        return optional(lop).map(
                objectL -> optional(rop).map(
                        objectL::equals
                ).orElse(true)
        ).orElse(true);
    }

    @Override
    public <N> boolean isNull(N obj) {
        return optional(obj).isPresent();
    }

    @Override
    public <N> void resolve(Consumer<N> func, N obj) {
        optional(obj).ifPresent(func);
    }

    @Override
    public <N, R> R resolve(Function<N, R> func, N obj, R orElse) {
        return optional(obj).map(func).orElse(orElse);
    }

    @Override
    public <N, P> void resolve(BiConsumer<N, P> func, N obj, P param) {
        optional(obj).ifPresent(object -> func.accept(object, param));
    }

    @Override
    public <N, P, R> R resolve(BiFunction<N, P, R> func, N obj, P param, R orElse) {
        return optional(obj).map(object -> func.apply(object, param)).orElse(orElse);
    }

    @Override
    public <N> N choose(N obj1, N obj2) {
        return optional(obj1).orElse(obj2);
    }

    @Override
    public <N> N chooseFrom(N[] objs) {
        return chooseFrom0(Arrays.asList(objs));
    }

    @Override
    public <N> List<N> chooseAllFrom(N[] objs) {
        List<N> list = new ArrayList<>();
        for (N obj : objs) {
            optional(obj).ifPresent(list::add);
        }
        return list;
    }
}
