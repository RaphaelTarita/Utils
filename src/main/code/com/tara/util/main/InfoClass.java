package com.tara.util.main;

import com.tara.util.java.hamming.EncodedHamming;
import com.tara.util.java.hamming.HammingCodec;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class InfoClass {
    private static final PrintStream OUT = System.out;

    public static void main(String[] args) {
        OUT.println(
                "This is the main class for java utils."
                        + "\nThe function main() displays this information."
                        + "\nThere is no other purpose of this Class."
        );

        List<Boolean> bits = new ArrayList<>(
                List.of(
                        false,
                        false,
                        true,
                        false,
                        true,
                        false,
                        true,
                        true
                )
        );

        EncodedHamming eh = new EncodedHamming(bits, 8);
        HammingCodec hc = HammingCodec.unprocessed(eh);
        hc.decode();
        OUT.println(hc.getDecoded().split());
    }
}
