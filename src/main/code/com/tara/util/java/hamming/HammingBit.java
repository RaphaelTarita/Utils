package com.tara.util.java.hamming;

public class HammingBit {
    private boolean bitVal;
    private final int bitIndex;
    private BitType bitType;

    private static boolean isInteger(double d) {
        return (d == Math.floor(d)) && !Double.isInfinite(d);
    }

    public HammingBit(boolean bit, int index, BitType all) {
        bitVal = bit;
        bitIndex = index;
        bitType = all;
    }

    public HammingBit(boolean bit, int index) {
        bitVal = bit;
        bitIndex = index;
        if (isInteger(Math.log(bitIndex) / Math.log(2))) {
            bitType = BitType.PARITY;
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

    public boolean isParity() {
        return bitType == BitType.PARITY;
    }

    public void flip() {
        bitVal = !bitVal;
    }

    @Override
    public String toString() {
        return (
                isParity()
                        ? "("
                        : ""
        )
                + (
                bitVal
                        ? '1'
                        : '0'
        )
                + (
                isParity()
                        ? ")"
                        : ""
        );
    }
}
