package com.tara.util.mirror;

public interface Mirrorable<C> {
    Mirrorable<C> mirrorWrap();
    C get();

    default C mirror() {
        return this.mirrorWrap().get();
    }
}
