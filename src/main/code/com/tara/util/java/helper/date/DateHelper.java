package com.tara.util.java.helper.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public final class DateHelper {

    private DateHelper() {
    }

    public static Calendar getCal() {
        return Calendar.getInstance();
    }

    public static Calendar getCal(Date date) {
        Calendar cal = getCal();
        cal.setTime(date);
        return cal;
    }

    public static Date getDate(Calendar cal) {
        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = getCal();
        cal.set(year, month - 1, day);
        return getDate(cal);
    }

    public static int getDay(Date d) {
        return getCal(d).get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay(Date d) {
        return getCal(d).get(Calendar.DAY_OF_WEEK);
    }

    public static int getMonth(Date d) {
        return getCal(d).get(Calendar.MONTH);
    }

    public static int getYear(Date d) {
        return getCal(d).get(Calendar.YEAR);
    }

    public static Date copyDate(Date date) {
        return new Date(date.getTime());
    }

    public static LocalDateTime convertLDT(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static LocalDate convertLD(Date date) {
        return convertLDT(date).toLocalDate();
    }

    public static LocalTime convertLT(Date date) {
        return convertLDT(date).toLocalTime();
    }

    public static Date reconvLDT(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date reconvLD(LocalDate date) {
        return reconvLDT(date.atStartOfDay());
    }

    public static Date reconvLT(LocalTime time) {
        return reconvLDT(time.atDate(LocalDate.now()));
    }
}
