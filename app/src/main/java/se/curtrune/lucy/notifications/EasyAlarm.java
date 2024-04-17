package se.curtrune.lucy.notifications;



import static se.curtrune.lucy.util.Logger.log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Locale;

import se.curtrune.lucy.classes.Notification;

public class EasyAlarm extends BroadcastReceiver {
    private static final int UNIQUE_ID = 2;
    private int hour;
    private int minutes;
    private String message;
    private String title;
    private Context context;
    private boolean repeat = false;
    private long repeatMilliSeconds;
    private EasyNotification easyNotification;
    public static final String INTENT_MESSAGE ="message";
    public static final String INTENT_TITLE="title";

    public EasyAlarm(Notification notification, Context context){
        log("EasyAlarm(Notification)");
        this.hour = notification.getTime().getHour();
        this.minutes = notification.getTime().getMinute();
        this.message = notification.getContent();
        this.title = notification.getTitle();
        this.context = context;

    }
    public void setAlarm(){
        log("...setAlarm");
        setAlarm(hour, minutes);

    }
    public static void cancelAlarm(int alarmID, Context context){
        log("...cancelAlarm(int)", alarmID);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    public void setRepeat(boolean repeat, long intervalMilliSeconds){
        log("...setRepeat(boolean)", repeat);
        this.repeat = repeat;
        this.repeatMilliSeconds = intervalMilliSeconds;
    }

    private void setAlarm(int hour, int minute){
        log("EasyAlarm.setAlarm()" + hour + ":" + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        startAlarm(calendar);
    }
    public void setRepeatingAlarm(int hour, int minute){
        log("...setRepeatingAlarm(int, int)");
    }

    private void startAlarm(Calendar calendar){
        log("EasyAlarm.startAlarm(Calender)");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if( alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent(context, EasyAlarm.class);
            intent.putExtra(INTENT_MESSAGE, message);
            intent.putExtra(INTENT_TITLE, title);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UNIQUE_ID, intent, PendingIntent.FLAG_MUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }else{
            log("...cant schedule exact alarm");
            log("...missing permission perhaps, doing it anyway");
            Intent intent = new Intent(context, EasyAlarm.class);
            intent.putExtra(INTENT_MESSAGE, message);
            intent.putExtra(INTENT_TITLE, title);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, UNIQUE_ID, intent, PendingIntent.FLAG_MUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        log("EasyAlarm.onReceive(Context, Intent)");
        String message = intent.getStringExtra(INTENT_MESSAGE);
        String title = intent.getStringExtra(INTENT_TITLE);
        easyNotification = new EasyNotification(context);
        easyNotification.sendNotificationCH1(title, message);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "alarm set %d:%d repeat: %b", hour, minutes, repeat);
    }
}
