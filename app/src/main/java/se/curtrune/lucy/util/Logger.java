package se.curtrune.lucy.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.statistics.CategoryListable;
import se.curtrune.lucy.statistics.StatisticsPeriod;

public class Logger {

    public static void log(CategoryListable listable){
        log("log(CategoryListable)");
        log("\tcategory", listable.getCategory());
    }
    public static void log(Estimate estimate){
        log("Logger.log(Estimate");
        log("\tenergy", estimate.getEnergy());
        log("\tduration", estimate.getDuration());

    }
    public static void log(Item item){
        log("Logger.log(Item)");
        if( item == null){
            log("log(Item) called with null item");
            return;
        }
        log("\tid", item.getID());
        log("\tparent_id ", item.getParentId());
        log("\thas child", item.hasChild());
        log("\theading", item.getHeading());
        log("\tcomment", item.getComment());
        log("\ttags", item.getTags());
        log("\tdescription", item.getDescription() );
        log("\tget info", item.getInfo());
        log("\tduration", item.getDuration());
        log("\tcreated", item.getCreated());
        log("\tupdated", item.getUpdated());
        log("\ttarget_date", item.getTargetDate());
        log("\ttarget_time", item.getTargetTime());
        //log("\tdays", item.getDays());
        log("\tstate", item.getState());
        log("\tcategory", item.getCategory());
        log("\ttype", item.getType());
        log("\tis template", item.isTemplate());
        log("\thas estimate", item.hasEstimate());
        if( item.hasEstimate()) {
            log("\titem has estimate");
            log("\t\tduration", item.getEstimate().getDuration());
            log("\t\tenergy", item.getEstimate().getEnergy());
        }
        log("\thas repeat", item.hasPeriod());
        if( item.hasPeriod()){
            log("\t\trepeat.toString", item.getPeriod().toString());
        }
        log("\thas mental", item.hasMental());
        log("\thas notification", item.hasNotification());
    }
    public static void log(Mental mental){
        log("log(Mental)");
        if(mental == null){
            log("log mental called with null mental");
            return;
        }
        log("\tid", mental.getID());
        log("\titemID", mental.getItemID());
        log("\theading", mental.getHeading());
        log("\tcategory", mental.getCategory());
        log("\tgetInfo()", mental.getInfo());
        log("\ttime", mental.getTime());
        log("\tcomment", mental.getComment());
        log("\tdate", mental.getDate());
        log("\tcreated", Converter.epochToFormattedDateTime(mental.getCreatedEpoch()));
        log("\tanxiety", mental.getAnxiety());
        log("\tmood", mental.getMood());
        log("\tenergy", mental.getEnergy());
        log("\tstress", mental.getStress());
    }
    public static void log(Notification notification){
        log("log(Notification)");
        log("\tdate", notification.getDate());
        log("\ttime", notification.getTime());
        log("\ttype", notification.getType().toString());
        log("\ttitle", notification.getTitle());
        log("\tcontent", notification.getContent());
        log("\ttoJson", notification.toJson());
    }
    public static void log(Repeat repeat){
        log("log(Repeat)");
        if( repeat == null){
            log("...repeat is null, i surrender");
            return;
        }
        log("\tmode", repeat.getMode().toString());
        if( repeat.isMode(Repeat.Mode.DAYS)){
            log("\tdays", repeat.getDays());
        }else{
            List<DayOfWeek> dayOfWeeks = repeat.getWeekDays();
            dayOfWeeks.forEach(System.out::println);
        }
        log("\tnextDate", repeat.getNextDate());
        log("\ttime", repeat.getTime());
        log("\ttoJson", repeat.toJson());
        log("\tfirstDate", repeat.getFirstDate());
        log("\tlastDate", repeat.getLastDate());
    }
    public static void log(StatisticsPeriod statisticsPeriod){
        log("log StatisticsPeriod...");
        log("\tfirstDate", statisticsPeriod.getFirstDate());
        log("\tlastDate", statisticsPeriod.getLastDate());
        log("\tduration", Converter.formatSecondsWithHours(statisticsPeriod.getDuration().getSeconds()));
        log("\taverage energy", statisticsPeriod.getAverageEnergy());
        log("\taverage anxiety", statisticsPeriod.getAverageAnxiety());
        log("\taverage mood", statisticsPeriod.getAverageMood());
        log("\taverage stress", statisticsPeriod.getAverageStress());


    }
    public static void log(String str){
        System.out.println(str);
    }
    public static void log(String str,Exception  e){
        log(str + ", " + e.getMessage());
    }
    public static void log(String description, boolean value)  {
        String message = String.format(Locale.getDefault(),"%s: %b",description, value);
        log(message);
    }
    public static void log(String description, int value){
        log(description + ": " + value);
    }
    public static void log(String description, float value){
        log(String.format(Locale.getDefault(),"%s: %f", description, value));
    }
    public static void log(String description, long value)  {
        String message = String.format(Locale.getDefault(),"%s: %d", description, value);
        log(message);
    }
    public static void log(String description, String value){
        log(description +", " + value);
    }
    public static void log(String description, LocalTime time){
        String message;
        if( time == null){
            message = String.format(Locale.getDefault(),"%s: null", description);
        }else {
            message = String.format(Locale.getDefault(), "%s %s", description, time);
        }
        log(message);
    }
    public static void log(String description, LocalDate value)  {
        String message;
        if( value == null){
            message = String.format(Locale.getDefault(),"%s: null",description);
        }else{
            message = String.format(Locale.getDefault(),"%s: %s",description, value);
        }
        log(message);
    }
    public static void log(String description, LocalDateTime value){
        log(description, value == null ? "": value.toString());
    }

    public static void log(String  description, State state){
        if(state == null){
            log(description, "null");
            return;
        }
        log(description, state.toString());
    }
    public static void log(String  description, Type type){
        if(type == null){
            log(description, "null");
            return;
        }
        log(description, type.toString());
    }
    public static void log(Transaction transaction){
        log("log(Transaction)...");
        log("\tid", transaction.getID());
        log("\tgoods", transaction.getHeading());
        log("\tamount", transaction.getAmount());
        log("\taccount", transaction.getAccount());
        log("\tdate", transaction.getDate());
        log("\ttype", transaction.getType().toString());
        log("\taccount ordinal", transaction.getAccountOrdinal());
        System.out.println();
    }
}
