package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.activities.DevActivity;
import se.curtrune.lucy.classes.LucindaInfo;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.util.Converter;

public class DevActivityViewModel extends ViewModel {
    private List<LucindaInfo> lucindaInfoList;
    private LucindaInfo createLucindaInfo(String key, String value){
        LucindaInfo lucindaInfo = new LucindaInfo();
        lucindaInfo.setKey(key);
        lucindaInfo.setValue(value);
        return lucindaInfo;
    }
    private LucindaInfo createLucindaInfo(String key, int value){
        LucindaInfo lucindaInfo = new LucindaInfo();
        lucindaInfo.setKey(key);
        lucindaInfo.setValue(value);
        return lucindaInfo;
    }
    public List<LucindaInfo> getLucindaInfo(){
        return lucindaInfoList;
    }
    public void init(Context context) {
        lucindaInfoList = new ArrayList<>();
        lucindaInfoList.add(createLucindaInfo("SDK_INT", Build.VERSION.SDK_INT));
        lucindaInfoList.add(createLucindaInfo("DEVICE", Build.DEVICE));
        lucindaInfoList.add(createLucindaInfo("USER", Build.USER));
        lucindaInfoList.add(createLucindaInfo("HARDWARE", Build.HARDWARE));
        lucindaInfoList.add(createLucindaInfo("MANUFACTURER", Build.MANUFACTURER));
        lucindaInfoList.add(createLucindaInfo("MODEL", Build.MODEL));
        //lucindaInfoList.add(createLucindaInfo("BASE_OS",Build.VERSION.BASE_OS));
        lucindaInfoList.add(createLucindaInfo("LOCAL_DB_VERSION", LocalDB.getDbVersion()));
        log("...init(DevActivity)");
        log("\tSDK_INT", Build.VERSION.SDK_INT);
        log("\tDEVICE", Build.DEVICE);
        log("\tUSER", Build.USER);
        log("\tHARDWARE", Build.HARDWARE);
        log("\tBRAND", Build.BRAND);
        log("\tMANUFACTURER", Build.MANUFACTURER);
        log("\tMODEL", Build.MODEL);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            //String version = pInfo.versionName;
            log("...versionName", pInfo.versionName);
            lucindaInfoList.add(createLucindaInfo("versionName", pInfo.versionName));

            LocalDateTime installDateTime = LocalDateTime.ofEpochSecond(pInfo.firstInstallTime /1000 , 0, ZoneOffset.UTC);
            lucindaInfoList.add(createLucindaInfo("first installed", installDateTime.toString()));
            LocalDateTime updateDateTime = LocalDateTime.ofEpochSecond(pInfo.lastUpdateTime / 1000, 0, ZoneOffset.UTC);
            lucindaInfoList.add(createLucindaInfo("last updated", updateDateTime.toString()));
            lucindaInfoList.add(createLucindaInfo("versionCode", pInfo.versionCode));
            log("...packageName", pInfo.packageName);
            lucindaInfoList.add(createLucindaInfo("packageName", pInfo.packageName));
            ApplicationInfo applicationInfo = pInfo.applicationInfo;
            log("...dataDir", applicationInfo.dataDir);
            lucindaInfoList.add(createLucindaInfo("dataDir", applicationInfo.dataDir));
        } catch (PackageManager.NameNotFoundException e) {
            log("NameNotFoundException", e.getMessage());
            e.printStackTrace();
        }catch (Exception e){
            log("Exception e", e.getMessage());
            e.printStackTrace();
        }
    }

    public void listColumns(Context context) {
        log("DevActivity.listColumns(Context)");
        try(LocalDB db = new LocalDB(context)){
            db.getColumns("items");
        }
    }
}
