package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.notifications.AlarmReceiver;

public class NotificationsWorker {
    public static final String CHANNEL_ONE = "CHANNEL_ONE";
    private static final String CHANNEL_ONE_NAME = "lucinda notifications";
    public static final String INTENT_MESSAGE ="INTENT_MESSAGE";
    public static final String INTENT_TITLE="INTENT_TITLE";
    public static final String INTENT_ITEM_ID = "INTENT_ITEM_ID";
    public static void cancelNotification(Item item, Context context){
        log("NotificationsWorker.cancelNotification(Item, Context) itemID", item.getID());
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) item.getID(), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


    }

    public static void createNotificationChannel(Context context){
        log("NotificationsWorker.createNotificationChannel(Context)");
        NotificationChannel notificationChannel1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel1 = new NotificationChannel(CHANNEL_ONE, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel1.enableLights(true);
            notificationChannel1.enableVibration(true);
            notificationChannel1.setLightColor(R.color.purple_200);
            notificationChannel1.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel1);
        }else{
            log("SDK_INT lower");
        }
    }

    public static void setNotifications(LocalDate date, Context context){
        log("NotificationsWorker.setNotifications(LocalDate)");
        List<Item> items = ItemsWorker.selectTodayList(date, context);
        for( Item item: items){
            if( item.hasNotification()){
                setNotification(item, context);
            }
        }
    }
    public static void setNotification(Item item, Context context){
        log("NotificationWorker.setNotification(Item)", item.getHeading());
        Notification notification = item.getNotification();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, notification.getDate().getYear());
        calendar.set(Calendar.MONTH, notification.getDate().getMonthValue() -1);
        calendar.set(Calendar.DATE, notification.getDate().getDayOfMonth() );
        calendar.set(Calendar.HOUR_OF_DAY,notification.getTime().getHour() );
        calendar.set(Calendar.MINUTE, notification.getTime().getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(INTENT_MESSAGE,item.getInfo());
        intent.putExtra(INTENT_TITLE, item.getHeading());
        intent.putExtra(INTENT_ITEM_ID, item.getID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) item.getID(), intent, PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            log("SDK VERSION GREATER OR EQUAL TO 31");
            if( alarmManager.canScheduleExactAlarms()) {
                log("...canScheduleExactAlarm");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //alarmManager.setRepeating();
/*                if(item.hasPeriod()){
                  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), , );
                }*/
            }else{
                log("...cant schedule exact alarm");
                log("...using nonExact");
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
    public void setNotification(Notification notification, Context context){
        log("...setNotification(Notification, Context)");

    }
}
