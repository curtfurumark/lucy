package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import se.curtrune.lucy.workers.SettingsWorker;

public class User {
    public static boolean VERBOSE = false;
    public static final String KEY_ICE_PHONE_NUMBER = "KEY_ICE_PHONE_NUMBER";
    public static final String USE_DARK_MODE = "USE_DARK_MODE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USES_PASSWORD = "USES_PASSWORD";
    public static final String KEY_CATEGORIES = "KEY_CATEGORIES";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String KEY_PANIC_ACTION = "KEY_PANIC_ACTION";
    public static final String KEY_PANIC_URLS = "KEY_PANIC_URLS";
    public static final String KEY_LANGUAGE = "KEY_LANGUAGE";
    public static final String KEY_DEV_MODE = "KEY_DEV_MODE";
    public static final String KEY_LOGGED_IN = "KEY_LOGGED_IN";
    public static final String KEY_FIRST_PAGE = "KEY_FIRST_PAGE";

    public static void addCategory(String category, Context context){
        log("User.addCategory(String, Context)", category);
        Set<String> setCategories = Settings.getSet(KEY_CATEGORIES, context);
        setCategories.add(category);
        Settings.saveSet( KEY_CATEGORIES, setCategories, context);
    }
    public static void addPanicUrl(String url, Context context) {
        log("User.addPanicUrl(String, Context)", url);
        Set<String> panicUrls = Settings.getSet(KEY_PANIC_URLS, context);
        panicUrls.add(url);
        Settings.saveSet(KEY_PANIC_URLS, panicUrls, context);
    }
    public static void deleteCategory(String category, Context context) {
        log("...deleteCategory(String)", category);
        List<String> categories = Settings.getList(KEY_CATEGORIES, context);
        boolean stat = categories.remove(category);
        if( !stat){
            log("ERROR removing category");
            return;
        }
        Settings.saveList(KEY_CATEGORIES, categories, context);

    }
    /**
     * list of categories, stored in sharedPreferences, settings whatever you want to call it
     * @param context, context, whatever that is
     * @return an array of categories
     */
    public static String[] getCategories(Context context){
        log("User.getCategories()");
        Set<String> setCategories = Settings.getSet(KEY_CATEGORIES, context);
        return Arrays.copyOf(setCategories.toArray(), setCategories.size(), String[].class);
    }
    public static boolean getDarkMode(Context context) {
        log("User.getDarkMode(Context)");
        return Settings.getBoolean(USE_DARK_MODE, false, context);
    }
    public static Settings.StartActivity getStartActivity(Context context) {
        int ordinal = Settings.getInt(KEY_FIRST_PAGE, Settings.StartActivity.TODAY_ACTIVITY.ordinal(), context);
        return Settings.StartActivity.values()[ordinal];
    }

    public static String getLanguage(Context context) {
        return Settings.getString(KEY_LANGUAGE, "sv", context);
    }
    public static Settings.PanicAction getPanicAction(Context context) {
        return Settings.PanicAction.valueOf(Settings.getString(KEY_PANIC_ACTION, Settings.PanicAction.SEQUENCE.toString(), context));
    }

    public static Set<String> getPanicUrls(Context context){
        return Settings.getSet(KEY_PANIC_URLS, context);
    }
    public static String getPassword(Context context) {
        log("User.getPassword()");
        return Settings.getString(KEY_PASSWORD, "", context);
    }

    public static String getRandomPanicUrl(Context context){
        log("User.getRandomPanicUrl(Context) ");
        List<String> urls = new ArrayList<>(getPanicUrls(context));
        log("...number of urls", urls.size());
        urls.forEach(System.out::println);
        Random random = new Random();
        return urls.get(random.nextInt(urls.size()));
    }
    public static boolean isDevMode(Context context) {
        return Settings.getBoolean(KEY_DEV_MODE, false, context);
    }

    public static void setIcePhoneNumber(int phoneNumber, Context context){
        Settings.addInt(KEY_ICE_PHONE_NUMBER, phoneNumber, context);
    }

    public static void setUsesPassword(boolean usesPassword, Context context){
        log("User.setUsesPassword(boolean, Context)", usesPassword);
        Settings.addBoolean(USES_PASSWORD, usesPassword, context);
    }
    public static boolean usesPassword(Context context) {
        if( VERBOSE) log("User.usesPassword(Context)");
        return Settings.getBoolean( USES_PASSWORD, false, context);
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


    public static void setFirstPage(Settings.StartActivity firstPage, Context context){
        log("User.setFirstPage(StartActivity, Context);", firstPage.toString());
        Settings.addInt(KEY_FIRST_PAGE, firstPage.ordinal(), context);
    }

    public static void setUseDarkMode(boolean darkMode, Context context) {
        log("User.setUseDarkMode(boolean, Context)");
        Settings.addBoolean(USE_DARK_MODE, darkMode, context);
        if( darkMode){
            SettingsWorker.setDarkMode();
        }else{
            SettingsWorker.setLightMode();
        }
    }

    public static void setPanicUrls(String[] urlArray, Context context) {
        log("User.setPanicUrls()");
        Set<String> urlSet = new HashSet<>(Arrays.asList(urlArray));
        Settings.saveSet(KEY_PANIC_URLS,urlSet, context );
    }

    public static void setDevMode(boolean devMode, Context context){
        Settings.addBoolean(KEY_DEV_MODE, devMode, context);
    }

    public static void setPanicAction(Settings.PanicAction panicAction, Context context) {
        Settings.addString(KEY_PANIC_ACTION, panicAction.toString(), context);
    }

    public static void setLanguage(String language, Context context) {
        log("User.setLanguage(String, Context)");
        Settings.addString(KEY_LANGUAGE, language , context);
        SettingsWorker.setLanguage(language);
    }

    public static void setPassword(String password, Context context) {
        log("User.setPassword(String, Context)");
        Settings.addString(KEY_PASSWORD, password, context);
    }

    public static void setCategories(String[] categories, Context context) {
        Set<String> urlSet = new HashSet<>(Arrays.asList(categories));
        Settings.saveSet(KEY_CATEGORIES,urlSet, context );
    }

    public static void deletePanicUrl(String url, Context context) {
        log("User.deletePanicUrl(String)", url);
        Set<String> urls = User.getPanicUrls(context);
        boolean stat = urls.remove(url);
        if( stat){
            User.setPanicUrls(urls.toArray(new String[1]), context);
        }else{
            log("ERROR removing url", url);
        }
    }

    public static int getICE(Context context) {
        return  Settings.getInt(KEY_ICE_PHONE_NUMBER, -1, context);
    }


}
