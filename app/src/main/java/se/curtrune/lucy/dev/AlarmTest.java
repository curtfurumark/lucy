package se.curtrune.lucy.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.AlarmClock;

public class AlarmTest {
    private static Ringtone ringtone;
    public static void setAlarm() {
        log("AlarmTest.setAlarm()");
    }
    public static void setAlarmUsingIntent(Context context, int hour, int minutes){
        log("AlarmTest.setAlarmUsingIntent()");
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        context.startActivity(intent);
    }
    public static void soundAlarm(Context context){
        log("AlarmTest.soundAlarm(Context)");
            // we will use vibrator first
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        //Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            log("...no TYPE_ALARM, setting sound to TYPE_NOTIFICATION");
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        // setting default ringtone
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        // play ringtone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(false);
        }
        ringtone.play();
    }
    public static void stopAlarm(){
        log("AlarmTest.stopAlarm()");
        if( ringtone == null){
            log("WARNING, no alarm to stop)");
        }
        ringtone.stop();
    }
}
