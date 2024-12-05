package se.curtrune.lucy.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;

import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.web.CheckForUpdateThread;
import se.curtrune.lucy.web.VersionInfo;

public class UpdaterTest {
    public static void checkForUpdate(Context context){
        log("...checkForUpdate()");
        CheckForUpdateThread thread = new CheckForUpdateThread(new CheckForUpdateThread.Callback() {
            @Override
            public void onRequestComplete(VersionInfo versionInfo, boolean res) {
                log("...onRequestComplete(VersionInfo, boolean)");
                log(versionInfo);
                //openUrl(versionInfo.getUrl(), context);
                PackageInfo packageInfo = Lucinda.getPackageInfo(context);
                if( packageInfo != null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if( packageInfo.getLongVersionCode() < versionInfo.getVersionCode()){
                            //log("update lucinda please");
                            openUrl(Constants.DOWNLOAD_LUCINDA_URL, context);
                        }
                    }else{
                        if( packageInfo.versionCode < versionInfo.getVersionCode()){
                            //log("update lucinda please");
                            openUrl(Constants.DOWNLOAD_LUCINDA_URL, context);
                        }
                    }
                }
            }
        });
        thread.start();
    }
    public static void openUrl(String url, Context context){
        log("UpdaterTest.openUrl(String)", url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

}
