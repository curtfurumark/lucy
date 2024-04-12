package se.curtrune.lucy.util;

import static se.curtrune.lucy.util.Logger.log;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Notification;

public class NotificationUtil {
    private static String  CHANNEL_ID = "crb";
    private static String CHANNEL_NAME = "lucinda";
    private static  String CHANNEL_DESCRIPTION ="lucinda notifications";
    private Notification notification;

    public NotificationUtil(Notification notification, Context context) {
        log("NotificationUtil(Notification, Context)");
        this.notification = notification;
        createNotification(context);
    }

    private void createNotification(Context context) {
        log("...createNotification(Context)");
/*        Intent intent = new Intent(context, AlertDetails);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = new PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_add_24)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getContent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/
    }
    public static void createNotificationChannel(Context context){
        log("...createNotificationChannel()");
        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            log("...notification channel created");
        }
    }
}
