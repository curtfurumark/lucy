package se.curtrune.lucy.util;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;



public class Converter {
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String TIME_FORMAT_PATTERN = "HH:mm";

    public static boolean VERBOSE = false;


    //2022-10-13


    public static String epochTimeToFormattedString(int time){
        return format(LocalTime.ofSecondOfDay(time));
    }
    //2020-11-14 12:15:59
    public static LocalDateTime epochToDateTime(long epoch){
        return LocalDateTime.ofEpochSecond(epoch, 0,ZoneOffset.UTC);
    }

    /**
     *
     * @param epoch seconds from 1 jan 1970
     * @return d formatted, if today, just time, any other date, just date
     */
    public static String epochDateTimeUI(long epoch){
        return formatUI(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC));
    }
    public static LocalDate epochToDate(long epoch){
        return LocalDate.ofEpochDay(epoch);
    }

    /**
     * valid values -365243219162L - 365241780471L
     * translated to datetime from 26th november -9605-11-26T22:13:58, to
     * the illustrious day 20th january  +13544-01-20T10:07:5
     * @param epoch
     * @return a LocalDateTime object
     */
    public static LocalDateTime epochToLocalDateTime(long epoch){
        return LocalDateTime.ofEpochSecond(epoch,0, ZoneOffset.UTC);

    }
    public static String epochToFormattedDateTime(long epoch){
        return formatUI(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC));
    }

    public static String formatTime(long secondOfDay){
        return LocalTime.ofSecondOfDay(secondOfDay).format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN));
    }

    public static String format(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
    }
    public static String format(LocalTime time){
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT_PATTERN));
    }
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
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

    public static String formatDateTimeUI(long dateTimeEpoch){
        return formatUI(LocalDateTime.ofEpochSecond(dateTimeEpoch, 0, ZoneOffset.UTC));
    }
    public static String formatMilliSeconds(long msecs){
            int secs =(int) (msecs / 1000) / 60;
            int mins =(int) (msecs / 1000) % 60;
            return  String.format("%02d:%02d", secs, mins);
    }


    public static String formatSeconds(int secs) {
        int isecs =  secs % 60;
        int mins =   (secs % 3600 )/ 60;
        return  String.format("%02d:%02d", mins , isecs );
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

    public static LocalTime secondsToLocalTime(long secs) {
        return LocalTime.ofSecondOfDay(secs);
    }

    /**
     * TODO, test
     * @param strDate, don't know how i want the date to be formatted
     * @return number of seconds since 1970 01 00 whatever, you know the stuff
     */
    public static long strDateToEpoch(String strDate){
        long epochDate = 0;
        if( strDate == null){
            if( VERBOSE) log("Converter.strDateToEpoch(String strDate) called with null strDate");
            return epochDate;
        }
        try {
            epochDate = LocalDate.parse(strDate).toEpochDay();
        }catch (NullPointerException npe){
            log("Converter.strDateToEpoch()", npe);
        }
        return epochDate;
    }
//    public static long strDateToEpoch(String strDate, String pattern){
//        return LocalDate.parse(strDate, DateTimeFormatter.ofPattern(pattern)).toEpochSecond(LocalTime.NOON, ZoneOffset.UTC);
//    }
    //ex, 2020-11-14 12:15:59
    public static long strDateTimeToEpoch(String strDateTime){
        if( VERBOSE) log("Converter.strDateTimeToEpoch(String strDateTime)", strDateTime);
        long epoch = 0;
        if( strDateTime == null || strDateTime.startsWith("0000-00-00")){
            return epoch;
        }
        try {
             epoch = LocalDateTime.parse(strDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toEpochSecond(ZoneOffset.UTC);
        }catch (DateTimeParseException dte){
            dte.printStackTrace();
            log("strDateTime ", strDateTime);
        }
        return epoch;
    }

    /**
     * @str , duration formatted as hh:mm:ss
     * @return
     */
    public static long strDurationToSeconds(String str){
        String[] parts = str.split(":");
        if( parts.length!= 3){
            log("...parse/format error,returnin 0");
            return 0;
        }
        return 0;
    }
    public static  int strTimeToSecondOfDay(String strTime){
        if( strTime == null){
            return 0;

        }
        return LocalTime.parse(strTime).toSecondOfDay();
    }



    /**
     * converts hh:mm:ss to seconds
     * @param str, format hh:mm:ss
     * @return duration of string in seconds
     */
    public static long stringToSeconds(String str){
        log("DateTimeStuff.stringToSeconds()", str);
        String[] arr = str.split(":");
        int hours = Integer.parseInt(arr[0]);
        int minutes = Integer.parseInt(arr[1]);
        int seconds = Integer.parseInt(arr[2]);
        return  hours * 3600L + minutes * 60L + seconds;
    }

    public static String formatDate(long date) {
        return LocalDate.ofEpochDay(date).format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
    }

    /**
     *
     * @param dateTime
     * @return hours and minutes if same day, full format otherwise
     */



}
