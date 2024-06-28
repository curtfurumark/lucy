package se.curtrune.lucy.notifications;



import static se.curtrune.lucy.util.Logger.log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;

public class EasyAlarm  {
    private static final int UNIQUE_ID = 2;

    public static final String INTENT_MESSAGE ="message";
    public static final String INTENT_TITLE="title";
    public static final String INTENT_ITEM_ID = "INTENT_ITEM_ID";
    private Calendar calendar;
    private Item item;

    public EasyAlarm(Item item){
        log("EasyAlarm(Item)", item.getHeading());
        this.item = item;
        this.calendar = Calendar.getInstance();
    }
    public void  setAlarm(Context context){
        log("...setAlarm");
        if( !item.hasNotification()){
            log("ERROR item without notification");
            return;
        }
        Notification notification = item.getNotification();
        //calendar.set(Calendar.MONTH, notification.getDate().getMonthValue());
        //calendar.set(Calendar.DATE, notification.getDate().getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY,notification.getTime().getHour() );
        calendar.set(Calendar.MINUTE, notification.getTime().getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        setAlarm(calendar, context);
    }
    public static void cancelAlarm(Item item, Context context){
        log("...cancelAlarm(Item, Context)", item.getHeading());
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) item.getID(), intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm(Calendar calendar, Context context){
        log("EasyAlarm.setAlarm(Calender, Context)");
        log(calendar);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(INTENT_MESSAGE,item.getInfo());
        intent.putExtra(INTENT_TITLE, item.getHeading());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) item.getID(), intent, PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            log("SDK VERSION GREATER OR EQUAL TO 31");
            if( alarmManager.canScheduleExactAlarms()) {
                log("...canScheduleExactAlarm");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }else{
                log("...cant schedule exact alarm");
                log("...using nonExact");
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }else{

        }
    }
}
