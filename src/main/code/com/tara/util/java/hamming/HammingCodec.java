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
            List<Boolean> givenParities = c.parities();
            List<Boolean> computedParities = computeParities(c);
            List<Integer> bitFlipParityIndexes = new ArrayList<>();
            for (int i = 0; i < givenParities.size(); i++) {
                if (givenParities.get(i) != computedParities.get(i)) {
                    bitFlipParityIndexes.add(i);
                }
            }
            int bitFlipIndex = -1;
            for (Integer i : bitFlipParityIndexes) {
                bitFlipIndex = (
                        bitFlipIndex == -1
                                ? i
                                : bitFlipIndex + i
                );
            }

            if (bitFlipIndex != -1) {
                c.at(bitFlipIndex).flip();
            }
            res.addAll(c.values());
        }

        decoded = new DecodedHamming(res, encoded.chunkSize());
        wasProcessed = true;
    }

    public void encode() {
        wasProcessed = true;
    }

    public DecodedHamming getDecoded() {
        if (!wasProcessed) {
            throw new IllegalStateException("Accessors are only available after processing");
        }
        return decoded;
    }

    public EncodedHamming getEncoded() {
        if (!wasProcessed) {
            throw new IllegalStateException("Accessors are only available after processing");
        }
        return encoded;
    }

    private List<Boolean> computeParities(HammingChunk c) {
        List<Boolean> res = new ArrayList<>();
        int checkIndex = 0;
        for (int i = 0; checkIndex < c.chunkSize(); i++, checkIndex = (int) Math.pow(2, i)) {
            Boolean current = null;
            for (HammingBit b : c.split()) {
                if ((checkIndex & (1 << b.index())) == 1) {
                    if (current == null) {
                        current = b.bit();
                    } else {
                        current = current != b.bit();
                    }
                }
            }
            res.add(current);
        }
        return res;
    }
}
