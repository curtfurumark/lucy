package se.curtrune.lucy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



public class Converter {
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SHORT_PATTERN = "dd/MM";
    public static final String TIME_FORMAT_PATTERN = "HH:mm";

    public static boolean VERBOSE = false;


    //2022-10-13


    public static String epochTimeToFormattedString(int time){
        return format(LocalTime.ofSecondOfDay(time));
    }
    //2020-11-14 12:15:59

    public static LocalDate epochToDate(long epoch){
        return LocalDate.ofEpochDay(epoch);
    }


    public static String epochToFormattedDateTime(long epoch){
        return formatUI(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC));
    }

    public static String formatTime(long secondOfDay){
        return LocalTime.ofSecondOfDay(secondOfDay).format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN));
    }

    @Deprecated
    public static String format(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
    }
    public static String formatShort(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_SHORT_PATTERN));
    }
    @Deprecated
    public static String format(LocalTime time){
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN));
    }

    /**
     *
     * @param dateTime, the dateTime to be converted to formatted string representation
     * @return hours and minutes if same day, full format otherwise
     */
    public static String formatUI(LocalDateTime dateTime) {
        if(dateTime.toLocalDate().equals(LocalDate.now())){
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return dateTime.format(DateTimeFormatter.ofPattern("E d/M - y"));
    }


  /**
     *
     * @param secs number of seconds to format
     * @return secs formatted HH:mm:ss
     */
    public static String formatSecondsWithHours(long secs){
        long  hours = secs / 3600;
        long  minutes = (secs % 3600) / 60;
        long  seconds = secs % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

}
