package com.tara.util.java.hamming;

import java.util.List;

public class DecodedHamming extends HammingBase {
    public DecodedHamming(byte[] rawInput, int chunkSize) {
        super(rawInput, chunkSize, false);
    }

    public DecodedHamming(List<Boolean> splitInput, int chunkSize) {
        super(splitInput, chunkSize, false);
    }
}
