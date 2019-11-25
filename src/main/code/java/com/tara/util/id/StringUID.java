package com.tara.util.id;

import java.util.HashSet;

public class StringUID implements UID<String> {
    private static final long serialVersionUID = 9114740774376532987L;

    private static HashSet<String> takenStrings = new HashSet<>();

    private String string;

    private void register(String str) {
        if (!taken(str)) {
            string = str;
            takenStrings.add(string);
        } else {
            throw new IllegalArgumentException("StringUID " + str + " is taken");
        }
    }

    public StringUID(String str) {
        register(str);
    }

    public StringUID() {
        //TODO add random char generator
        String str = "random";
        while (taken(str)) {
            str = "newRandom";
        }
        register(str);
    }

    @Override
    public boolean taken(String idGenerator) {
        return takenStrings.contains(idGenerator);
    }

    @Override
    public UID<String> newUnique(String generator) {
        return taken(generator)
                ? new StringUID()
                : new StringUID(generator);
    }

    @Override
    public String getGenerator() {
        return null;
    }
}
