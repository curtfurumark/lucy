package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Set;

import se.curtrune.lucy.fragments.CustomizeFragment;

public class User {
    public static final String ICE_PHONE_NUMBER = "ICE_PHONE_NUMBER";
    public static final String USER_NAME = "USER_NAME";
    public static final String USES_PASSWORD = "USES_PASSWORD";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String KEY_PANIC_URLS = "KEY_PANIC_URLS";
    public static String getRandomPanicUrl(){
        return "https://bongo.cat";
    }


    public static Set<String> getPanicUrls(Context context){
        return Settings.getList(KEY_PANIC_URLS, context);
    }
    public static String getUserName(Context context){
        return Settings.getString(USER_NAME, "anonymous", context);
    }
    public static boolean hasPassword(Context context) {
        String pwd = Settings.getString(KEY_PASSWORD, "", context);
        return !pwd.isEmpty();
    }
    public static void setIcePhoneNumber(int phoneNumber, Context context){
        Settings.addInt(ICE_PHONE_NUMBER, phoneNumber, context);
    }
    public static void setUserName(String userName, Context context){
        Settings.addString(Settings.USER,userName,  context);
    }
    public static void setUsesPassword(boolean usesPassword, Context context){
        log("User.setUsesPassword(boolean, Context)", usesPassword);
        Settings.addBoolean(USES_PASSWORD, usesPassword, context);
    }
    public static boolean usesPassword(Context context) {
        log("User.usesPassword()");
        return Settings.getBoolean( USES_PASSWORD, true, context);
    }
    public static boolean validatePassword(String user, String pwd, Context context){
        log("...validatePassword(String, String)");
        String password = Settings.getString(KEY_PASSWORD, "", context);
        return password.equals(pwd);
    }


    public static void savePassword(String pwd, Context context) {
        log("User.savePassword(String, Context)");
        Settings.addString(KEY_PASSWORD, pwd, context);
    }

    public static String getPassword(Context context) {
        log("User.getPassword()");
        return Settings.getString(KEY_PASSWORD, "", context);
    }

    public static void addPanicUrl(String url, Context context) {
        log("User.addPanicUrl(String, Context)", url);
        Set<String> panicUrls = Settings.getList(KEY_PANIC_URLS, context);
        panicUrls.add(url);
        Settings.saveList(KEY_PANIC_URLS, panicUrls, context);
    }
}
