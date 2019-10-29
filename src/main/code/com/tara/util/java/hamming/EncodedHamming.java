package com.tara.util.java.hamming;

import java.util.List;

public class EncodedHamming extends HammingBase {
    public EncodedHamming(byte[] rawInput, int chunkSize) {
        super(rawInput, chunkSize);
    }

    public EncodedHamming(List<Boolean> splitInput, int chunkSize) {
        super(splitInput, chunkSize);
    }
}
