package com.hotelmanagement.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {
    private DateUtils() {}

    public static LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toUtilDate(LocalDate date) {
        if (date == null) return null;
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static java.sql.Date toSqlDate(LocalDate date) {
        if (date == null) return null;
        return java.sql.Date.valueOf(date);
    }

    public static LocalDate fromSqlDate(java.sql.Date date) {
        if (date == null) return null;
        return date.toLocalDate();
    }

    public static LocalDate fromDatePicker(Date date) {
        return toLocalDate(date);
    }
}
