package se.curtrune.lucy.util;

import com.jjoe64.graphview.series.DataPoint;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.classes.Contact;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.ItemStatistics;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.DateHourCell;
import se.curtrune.lucy.persist.DB1Result;
import se.curtrune.lucy.statistics.CategoryListable;
import se.curtrune.lucy.statistics.StatisticsPeriod;
import se.curtrune.lucy.web.HTTPRequest;

public class Logger {

    public static void log(Calendar calendar){
        log("log(Calendar)");
        log("\tyear", calendar.get(Calendar.YEAR));
        log("\tmonth", calendar.get(Calendar.MONTH -1));
        log("\tdate", calendar.get(Calendar.DATE));
        log("\thour", calendar.get(Calendar.HOUR_OF_DAY));
        log("\tminute", calendar.get(Calendar.MINUTE));
    }

    /**
     * precision 2 decimals
     * @param dataPoint
     */
    public static void log(DataPoint dataPoint){
        log("Logger.log(DataPoint)");
        log(String.format(Locale.getDefault(), "x: %.2f, y: %.2f", dataPoint.getX(), dataPoint.getY()));


    }
    public static void log(CategoryListable listable){
        log("log(CategoryListable)");
        log("\tcategory", listable.getCategory());
    }
    public static void log(DateHourCell dateHourCell){
        log("log(DateHourCell)");
        log("\thour", dateHourCell.getHour());
        log("\tdate", dateHourCell.getDate());
    }
    public static void log(DB1Result result){
        log("CRBLogger.log(DB1Result)...");
        log("\tid", result.getID());
        log("\tphp_file", result.getPhpFile());
        log("\tis ok", result.isOK());
        log("\tsql", result.getSql());
        log("\tjson", result.getJson());
        log("\tmessage count: ", result.getMessages() == null? 0: result.getMessages().length);
        if( null != result.getMessages()) {
            for (String message : result.getMessages()) {
                log("\t\tmessage", message);
            }
        }
    }
    public static void log(String description, double value){
        log(description + ": " + value);

    }
    public static void log(HTTPRequest request){
        log("log(HTTPRequest)");
        if( request == null){
            log("...request is null");
            return;
        }
        log("...url", request.getUrl());
        log("...http method", request.getHttpMethod().toString());
        Map<String, String> params = request.getParams();
        for( String key: params.keySet()){
            log("......"  +key, params.get(key));
        }
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
        log("\tdescription", item.getDescription() );//TODO, not in use
        log("\tget info", item.getInfo());
        log("\tduration", item.getDuration());
        log("\tcreated", item.getCreated());
        log("\tupdated", item.getUpdated());
        log("\ttarget_date", item.getTargetDate());
        log("\ttarget_time", item.getTargetTime());
        log("\tstate", item.getState());
        log("\tcategory", item.getCategory());
        log("\ttype", item.getType());
        log("\tis template", item.isTemplate());
        log("\thas estimate", item.hasEstimate());
        log("\t\tisCalenderItem", item.isCalenderItem());
        if( item.hasEstimate()) {
            log("\titem has estimate");
/*            log("\t\tduration", item.getEstimate().getDuration());
            log("\t\tenergy", item.getEstimate().getEnergy());*/
        }
        log("\thas repeat", item.hasPeriod());
        if( item.hasPeriod()){
            log("\t\trepeat.toString", item.getPeriod().toString());
        }
        log("\thas notification", item.hasNotification());
        log("\tcolor", item.getColor());
        log("\tpriority", item.getPriority());
        log("\t\tanxiety", item.getAnxiety());
        log("\t\tenergy", item.getEnergy());
        log("\t\tmood", item.getMood());
        log("\t\tstress", item.getStress());
        log("\t\trepeat_id", item.getRepeatID());
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
        log("\tisDone", mental.isDone());
        log("\ttime", mental.getTime());
        log("\tcomment", mental.getComment());
        log("\tdate", mental.getDate());
        log("\tcreated", Converter.epochToFormattedDateTime(mental.getCreatedEpoch()));
        log("\tanxiety", mental.getAnxiety());
        log("\tmood", mental.getMood());
        log("\tenergy", mental.getEnergy());
        log("\tstress", mental.getStress());
        log("\tisTemplate", mental.isTemplate());
    }
    public static void log(Message message){
        log("Logger.log(Message)");
        log("\tsubject", message.getSubject());
        log("\tcontent", message.getContent());
        log("\tuser", message.getUser());
        log("\tcreated", message.getCreated());
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
        log("\tid", repeat.getID());
        log("\tunit", repeat.getUnit().toString());
        log("\tqualifier", repeat.getQualifier());
        log("\ttoJson", repeat.toJson());
        log("\tfirstDate", repeat.getFirstDate());
        log("\tlastDate", repeat.getLastDate());
        log("\tinfinity", repeat.isInfinite());
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
    public static void log(ItemStatistics itemStatistics){
        log("log(ItemStatistics)");
        if(itemStatistics == null){
            log("WARNING itemStatistics is null");
            return;
        }
        log("\tnumber of items", itemStatistics.getNumberOfItems());
        log("\ttotal duration", Converter.formatSecondsWithHours( itemStatistics.getDuration()));
        log("\taverage energy", itemStatistics.getAverageEnergy());
        log("\ttotal energy", itemStatistics.getEnergy());
        log("\ttotal anxiety", itemStatistics.getAnxiety());
        log("\taverage anxiety", itemStatistics.getAverageAnxiety());
        log("\ttotal stress", itemStatistics.getStress());
        log("\ttotal mood", itemStatistics.getMood());
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
    public static void log(URL url) {
        log("log(URL)");
        log("...getHost", url.getHost());
        log("...getPort", url.getPort());
        log("...getProtocol", url.getProtocol());
        log("...getQuery", url.getQuery());
        log("...getUserInfo", url.getUserInfo());
        log("...getAuthority", url.getAuthority());
        log("...toExternalForm", url.toExternalForm());
    }
    public static void logDateHourCells(List<DateHourCell> dateHourCells){
        assert dateHourCells != null;
        log("Logger.logDateHourCells(List<DateHourCell>)");
        for(int i = 0; i < dateHourCells.size(); i++){
            String str = String.format(Locale.getDefault(),"%d %s", i, dateHourCells.get(i).toString() );
            log(str);
        }
    }

    public static void log(Contact contact) {
        log("log(Contact)");
        if( contact == null){
            log("cannot log a null contact");
            return;
        }
        log("\tdisplayName", contact.getDisplayName());
        log("\tphoneNumber", contact.getPhoneNumber());
        log("\temail", contact.getEmail());
        log("\tid", contact.getId());
    }
}
