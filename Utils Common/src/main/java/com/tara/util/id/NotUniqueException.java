package com.tara.util.id;

public class NotUniqueException extends RuntimeException {
    private final UID notUnique;

    public NotUniqueException(UID id) {
        notUnique = id;
    }

    @Override
    public String toString() {
        return notUnique.toString() + " is not unique according to the instance registry";
    }
}
