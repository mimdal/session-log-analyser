package com.github.mimdal.tools.log.util;

import java.time.LocalDateTime;
import java.time.format.*;
import java.time.temporal.ChronoField;

import static com.github.mimdal.tools.log.constants.Constants.TIME_DATE_FORMAT;

/**
 * @author M.dehghan
 * @since 2020-07-30
 */
public class Utils {

    public static void systemExit(String msg) {
        System.out.println(msg);
        System.exit(84);
    }

    public static String formatNumber(long number) {
        return String.format("%,8d%n", number);
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .appendPattern(TIME_DATE_FORMAT)
                .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 9, true)
                .toFormatter();
    }

    public static LocalDateTime stringToDate(String stringDate) {
        return LocalDateTime.parse(stringDate, getDateTimeFormatter());
    }


}
