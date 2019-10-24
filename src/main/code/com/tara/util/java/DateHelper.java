package com.tara.util.java;

import java.util.Calendar;
import java.util.Date;

public final class DateHelper {
    private static final Calendar CAL = Calendar.getInstance();

    private DateHelper() {
    }

    public static Date getDate(int year, int month, int day) {
        CAL.set(year, month - 1, day);
        return CAL.getTime();
    }
}
