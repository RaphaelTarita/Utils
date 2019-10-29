package com.tara.util.java.hamming;

import java.util.ArrayList;
import java.util.List;

public class HammingChunk {
    private static final int[] MASKS = new int[]{
            0x1,
            0x2,
            0x4,
            0x8,
            0x10,
            0x20,
            0x40,
            0x80
    };
    private byte[] rawData;
    private List<HammingBit> splitData = new ArrayList<>();

    public HammingChunk(byte[] rawInput) {
        rawData = rawInput.clone();
        splitUp();
    }

    public HammingChunk(List<Boolean> splitInput) {
        int count = 0;
        for (Boolean b : splitInput) {
            splitData.add(new HammingBit(b, count));
            count++;
        }
        merge();
    }

    public void splitUp() {
        int count = 0;
        for (byte b : rawData) {
            for (int mask : MASKS) {
                splitData.add(new HammingBit((b & mask) != 0, count));
                count++;
            }
        }
    }

    public void merge() {
        rawData = new byte[(int) Math.ceil(splitData.size() / 8.0)];
        for (int i = 0; i < rawData.length; i++) {
            byte val = 0;
            int count = 0;
            for (HammingBit b : splitData) {
                if (count++ >= 8) {
                    break;
                }
                val <<= 1;
                if (b.bit()) {
                    val |= 1;
                }
            }
            rawData[i] = val;
        }
    }

    public byte[] raw() {
        return rawData.clone();
    }

    public List<HammingBit> split() {
        return splitData;
    }

    public List<Boolean> checks() {
        List<Boolean> res = new ArrayList<>();
        for (HammingBit b : splitData) {
            if (b.isCheck()) {
                res.add(b.bit());
            }
        }
        return res;
    }

    public List<Boolean> values() {
        List<Boolean> res = new ArrayList<>();
        for (HammingBit b : splitData) {
            if (b.isValue()) {
                res.add(b.bit());
            }
        }
        return res;
    }

    public int chunkSize() {
        if (splitData != null) {
            return splitData.size();
        } else if (rawData != null) {
            return rawData.length;
        } else {
            return 0;
        }
    }

    public HammingBit at(int index) {
        return splitData.get(index);
    }
}
