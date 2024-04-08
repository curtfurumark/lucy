package se.curtrune.lucy.util;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Notification;

public class NotificationUtil {
    private static String  CHANNEL_ID = "crb";
    private Notification notification;

    public NotificationUtil(Notification notification, Context context) {
        log("NotificationUtil(Notification, Context)");
        this.notification = notification;
        createNotification(context);
    }

    private void createNotification(Context context) {
        log("...createNotification(Context)");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_add_24)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getContent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
