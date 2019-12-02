package com.tara.util.tools.cache;

import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Cacher<I, O> implements Mirrorable<Cacher<I, O>> {
    private Map<I, O> cache;
    private Function<I, O> function;

    public Cacher(Cacher<I, O> from) {
        this.function = from.function;
        this.cache = from.cache;
    }

    public Cacher(Function<I, O> function) {
        this.function = function;
        cache = new HashMap<>();
    }

    public List<I> cause(O output) {
        List<I> res = new ArrayList<>();
        if (cache.containsValue(output)) {
            for (Map.Entry<I, O> entry : cache.entrySet()) {
                if (entry.getValue().equals(output)) {
                    res.add(entry.getKey());
                }
            }
        }
        return res;
    }

    public O direct(I input) {
        return function.apply(input);
    }

    public O cache(I input) {
        O res = direct(input);
        cache.putIfAbsent(input, res);
        return res;
    }

    public void cache(I[] inputs) {
        for (I input : inputs) {
            cache(input);
        }
    }

    public void cache(List<I> inputs) {
        for (I input : inputs) {
            cache(input);
        }
    }

    public O forceCache(I input) {
        O res = direct(input);
        cache.put(input, res);
        return res;
    }

    public O fromCache(I input) {
        return cache.get(input);
    }

    public O computeFast(I input) {
        return cache.computeIfAbsent(input, output -> direct(input));
    }

    public O compute(I input) {
        if (cache.containsKey(input)) {
            return cache.get(input);
        } else {
            return cache(input);
        }
    }

    @Override
    public Cacher<I, O> mirror() {
        Cacher<I, O> cacher = new Cacher<>(function);
        cacher.cache = Mirrors.mirror(cache);
        return cacher;
    }
}
