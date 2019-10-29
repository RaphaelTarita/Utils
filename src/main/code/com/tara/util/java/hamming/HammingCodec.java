package com.tara.util.java.hamming;

import java.util.ArrayList;
import java.util.List;

public class HammingCodec {
    private DecodedHamming decoded;
    private EncodedHamming encoded;
    private boolean wasProcessed;

    public static HammingCodec unprocessed(HammingBase input) {
        return new HammingCodec(input, false);
    }

    private HammingCodec(HammingBase input, boolean wasProcessed) {
        this.wasProcessed = wasProcessed;
        if (input instanceof DecodedHamming) {
            decoded = (DecodedHamming) input;
        } else if (input instanceof EncodedHamming) {
            encoded = (EncodedHamming) input;
        } else {
            throw new IllegalArgumentException("HammingData is not of a known type");
        }
    }

    public HammingCodec(DecodedHamming decodedInput) {
        this(decodedInput, true);
        encode();
    }

    public HammingCodec(EncodedHamming encodedInput) {
        this(encodedInput, true);
        decode();
    }

    public HammingCodec(HammingBase input) {
        this(input, true);
        if (encoded == null) {
            encode();
        } else if (decoded == null) {
            decode();
        }
    }

    public void decode() {
        List<Boolean> res = new ArrayList<>();
        for (HammingChunk c : encoded) {
            List<Boolean> givenChecks = c.checks();
            List<Boolean> computedChecks = computeChecks(c);
            // TODO
        }

        decoded = new DecodedHamming(res, encoded.chunkSize());
        wasProcessed = true;
    }

    public void encode() {
        wasProcessed = true;
    }

    private List<Boolean> computeChecks(HammingChunk c) {
        List<Boolean> res = new ArrayList<>();
        List<Boolean> values = c.values();
        int checkIndex = 0;
        for (int i = 0; i < c.chunkSize(); i++, checkIndex = (int) Math.pow(2, i)) {
            // TODO
        }
        return res;
    }
}
