package com.tara.util.misc;

public class UByte {
    private static final int OFFSET = Byte.MAX_VALUE + 1;
    private byte inner;

    private static byte transformByte(int unsigned) {
        if (unsigned < 0) {
            throw new IllegalArgumentException("UByte is exclusively for unsigned data");
        }

        return (byte) (unsigned - OFFSET);
    }

    private static int transformInt(byte signed) {
        return signed + OFFSET;
    }

    private UByte(byte raw) {
        inner = raw;
    }

    public UByte(int unsigned) {
        this(transformByte(unsigned));
    }

    public UByte() {
        this(0);
    }

    public int get() {
        return transformInt(inner);
    }

    public void set(int unsigned) {
        inner = transformByte(unsigned);
    }

    public int rawLeftShift(UByte rop) {
        return get() << rop.get();
    }

    public int rawRightShift(UByte rop) {
        return get() >> rop.get();
    }

    public int rawAnd(UByte rop) {
        return get() & rop.get();
    }

    public int rawOr(UByte rop) {
        return get() | rop.get();
    }

    public UByte leftShift(UByte rop) {
        return new UByte(
                rawLeftShift(rop)
        );
    }

    public UByte rightShift(UByte rop) {
        return new UByte(
                rawRightShift(rop)
        );
    }

    public UByte and(UByte rop) {
        return new UByte(
                rawAnd(rop)
        );
    }

    public UByte or(UByte rop) {
        return new UByte(
                rawOr(rop)
        );
    }

    public int add(UByte rop) {
        return get() + rop.get();
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public static int rawLeftShift(UByte lop, UByte rop) {
        return lop.rawLeftShift(rop);
    }

    public static int rawRightShift(UByte lop, UByte rop) {
        return lop.rawRightShift(rop);
    }

    public static int rawAnd(UByte lop, UByte rop) {
        return lop.rawAnd(rop);
    }

    public static int rawOr(UByte lop, UByte rop) {
        return lop.rawOr(rop);
    }

    public static UByte leftShift(UByte lop, UByte rop) {
        return lop.leftShift(rop);
    }

    public static UByte rightShift(UByte lop, UByte rop) {
        return lop.rightShift(rop);
    }

    public UByte and(UByte lop, UByte rop) {
        return lop.and(rop);
    }

    public UByte or(UByte lop, UByte rop) {
        return lop.or(rop);
    }

    public static UByte leftShift(int lop, UByte rop) {
        return new UByte(
                lop << rop.get()
        );
    }

    public static UByte rightShift(int lop, UByte rop) {
        return new UByte(
                lop >> rop.get()
        );
    }

    public static UByte and(int lop, UByte rop) {
        return new UByte(
                lop & rop.get()
        );
    }

    public static UByte or(int lop, UByte rop) {
        return new UByte(
                lop | rop.get()
        );
    }
}
