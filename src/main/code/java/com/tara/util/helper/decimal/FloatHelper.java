package com.tara.util.helper.decimal;

public class FloatHelper {
    private FloatHelper() {
    }

    private static double epsilon = 0.00001;

    public static void setEpsilon(double newEpsilon) {
        epsilon = newEpsilon;
    }

    public static boolean epsilonEquals(double lop, double rop) {
        return (lop - epsilon) < rop && (lop + epsilon) > rop;
    }

    public static int epsilonCompare(double lop, double rop) {
        if (epsilonEquals(lop, rop)) {
            return 0;
        } else if (lop < rop) {
            return -1;
        } else {
            return 1;
        }
    }

    public static int floor(double op) {
        return (int) op;
    }

    public static int ceil(double op) {
        return (int) (op + 1);
    }

    public static int roundToInt(double op, int split) {
        if (op - floor(op) < (double) split / 10.0) {
            return floor(op);
        } else {
            return ceil(op);
        }
    }

    public static int roundToInt(double op) {
        return roundToInt(op, 5);
    }
}
