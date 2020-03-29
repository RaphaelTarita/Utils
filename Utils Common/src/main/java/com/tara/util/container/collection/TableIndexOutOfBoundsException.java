package com.tara.util.container.collection;

import com.google.common.base.Strings;

public class TableIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private final Orientation orientation;
    private final String message;

    public TableIndexOutOfBoundsException(Orientation orientation, int index, String message) {
        super(index);
        this.orientation = orientation;
        this.message = message;
    }

    public TableIndexOutOfBoundsException(Orientation orientation, int index) {
        this(orientation, index, null);
    }

    public TableIndexOutOfBoundsException(String message) {
        super(message);
        this.message = message;
        orientation = null;
    }

    public TableIndexOutOfBoundsException(int index) {
        super(index);
        orientation = null;
        message = null;
    }

    @Override
    public String getMessage() {
        String superMessage = super.getMessage();
        return Strings.isNullOrEmpty(superMessage) ? message : superMessage;
    }

    @Override
    public String toString() {
        return getMessage() + (
            orientation != null
                ? ", overflowed orientation: " + orientation.symbol()
                : "");
    }
}
