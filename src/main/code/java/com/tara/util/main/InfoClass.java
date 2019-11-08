package com.tara.util.main;

import com.tara.util.helper.decimal.FloatHelper;

import java.io.PrintStream;

public class InfoClass {
    private static final PrintStream OUT = System.out;

    public static void main(String[] args) {
        OUT.println(
                "This is the main class for java utils."
                        + "\nThe function main() displays this information."
                        + "\nThere is no other purpose of this Class."
        );

        OUT.println(FloatHelper.roundToInt(10.3));
    }
}
