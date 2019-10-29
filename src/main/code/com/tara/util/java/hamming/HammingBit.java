package com.tara.util.java.hamming;

public class HammingBit {
    private boolean bitVal;
    private final int bitIndex;
    private BitType bitType;

    private static boolean isInteger(double d) {
        return (d == Math.floor(d)) && !Double.isInfinite(d);
    }

    public HammingBit(boolean bit, int index) {
        bitVal = bit;
        bitIndex = index;
        if (isInteger(Math.log(bitIndex) / Math.log(2))) {
            bitType = BitType.CHECK;
        } else {
            bitType = BitType.VALUE;
        }
    }

    public boolean bit() {
        return bitVal;
    }

    public int index() {
        return bitIndex;
    }

    public BitType type() {
        return bitType;
    }

    public boolean isValue() {
        return bitType == BitType.VALUE;
    }

    public boolean isCheck() {
        return bitType == BitType.CHECK;
    }
}
