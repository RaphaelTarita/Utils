package com.tara.util.persistence.http;

public enum RequireType {
    GIVEN,
    MAY,
    CONDITIONAL,
    NOT_GIVEN;

    public boolean can() {
        return this == GIVEN
            || this == MAY
            || this == CONDITIONAL;
    }
}
