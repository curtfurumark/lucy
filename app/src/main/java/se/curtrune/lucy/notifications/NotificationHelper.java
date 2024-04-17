package se.curtrune.lucy.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import se.curtrune.lucy.R;


public class NotificationHelper extends ContextWrapper {
    private static final String CHANNEL_ONE = "channel1";
    private static final String CHANNEL_ONE_NAME ="42:1";
    private static final String CHANNEL_TWO = "channel2";
    private static final String CHANNEL_TWO_NAME ="42:2";
    private NotificationManager notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ONE, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel1.enableLights(true);
        notificationChannel1.enableVibration(true);
        notificationChannel1.setLightColor(R.color.purple_200);
        notificationChannel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel1);

        NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_TWO, CHANNEL_TWO_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel2.enableLights(true);
        notificationChannel2.enableVibration(true);
        notificationChannel2.setLightColor(R.color.purple_200);
        notificationChannel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel2);
    }
    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        return  new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ONE )
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_icecream_24);
    }
    public NotificationCompat.Builder getChannel2Notification(String title, String message) {
        return  new NotificationCompat.Builder(getApplicationContext(), CHANNEL_TWO )
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_icecream_24);
    }
}
