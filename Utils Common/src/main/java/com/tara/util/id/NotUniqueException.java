package com.tara.util.id;

public class NotUniqueException extends RuntimeException {
    private final UID notUnique;

    public NotUniqueException(UID id) {
        notUnique = id;
    }

    public UID getDuplicate() {
        return notUnique;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + notUnique.toString() + " (" + notUnique.mapUID() + ") is not unique according to the instance registry";
    }
}
