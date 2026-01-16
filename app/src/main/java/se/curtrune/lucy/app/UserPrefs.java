package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import se.curtrune.lucy.persist.SettingsStore;
import se.curtrune.lucy.screens.settings.MentalFlag;
import se.curtrune.lucy.screens.settings.PanicOption;
import se.curtrune.lucy.workers.SettingsWorker;

public class UserPrefs {
    private static final String KEY_GOOGLE_CALENDAR_ID = "KEY_GOOGLE_CALENDAR_ID";
    private static final String KEY_SHOW_MENTAL_STATUS = "KEY_SHOW_MENTAL_STATUS";
    private static final String KEY_MENTAL_FLAG = "KEY_MENTAL_FLAG";
    private static final String KEY_SHOW_MEDICINE = "KEY_SHOW_MEDICINE";
    public static boolean VERBOSE = false;
    public static final String KEY_ICE_PHONE_NUMBER = "KEY_ICE_PHONE_NUMBER";
    public static final String USE_DARK_MODE = "USE_DARK_MODE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USES_PASSWORD = "USES_PASSWORD";
    public static final String KEY_CATEGORIES = "KEY_CATEGORIES";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String SYNC_WITH_GOOGLE_CALENDAR = "SYNC_WITH_GOOGLE_CALENDAR";
    public static final String KEY_PANIC_ACTION = "KEY_PANIC_ACTION";
    public static final String KEY_PANIC_URLS = "KEY_PANIC_URLS";
    public static final String KEY_LANGUAGE = "KEY_LANGUAGE";
    public static final String KEY_DEV_MODE = "KEY_DEV_MODE";
    public static final String KEY_LOGGED_IN = "KEY_LOGGED_IN";
    public static final String KEY_INITIAL_SCREEN = "KEY_INITIAL_SCREEN";
    public static final String KEY_SCROLL_POSITION_DAY_CALENDAR = "KEY_SCROLL_POSITION_DAY_CALENDAR";

    public static final String KEY_SHOW_APPOINTMENTS = "KEY_SHOW_APPOINTMENTS";
    public static final String KEY_SHOW_TIMELINE = "KEY_SHOW_TIMELINE";
    public static final String KEY_SHOW_DEV_SCREEN = "KEY_SHOW_DEV_SCREEN";
    public static final String KEY_SHOW_PROJECTS = "KEY_SHOW_PROJECTS";
    public static final String KEY_SHOW_DURATION = "KEY_SHOW_DURATION";
    public static final String KEY_SHOW_MENTAL_STATS_SCREEN = "KEY_SHOW_MENTAL_STATS_SCREEN";
    public static final String KEY_SHOW_TODO = "KEY_SHOW_TODO";
    public static void addCategory(String category, Context context){
        log("UserPrefs.addCategory(String, Context)", category);
        Set<String> setCategories = SettingsStore.getSet(KEY_CATEGORIES, context);
        setCategories.add(category);
        SettingsStore.saveSet( KEY_CATEGORIES, setCategories, context);
    }
    public static void addPanicUrl(String url, Context context) {
        log("UserPrefs.addPanicUrl(String, Context)", url);
        Set<String> panicUrls = SettingsStore.getSet(KEY_PANIC_URLS, context);
        panicUrls.add(url);
        SettingsStore.saveSet(KEY_PANIC_URLS, panicUrls, context);
    }
    public static void deleteCategory(String category, Context context) {
        log("...deleteCategory(String)", category);
        List<String> categories = SettingsStore.getList(KEY_CATEGORIES, context);
        boolean stat = categories.remove(category);
        if( !stat){
            log("ERROR removing category");
            return;
        }
        SettingsStore.saveList(KEY_CATEGORIES, categories, context);

    }
    /**
     * list of categories, stored in sharedPreferences, settings whatever you want to call it
     * @param context, context, whatever that is
     * @return an array of categories
     */
    public static String[] getCategories(Context context){
        log("UserPrefs.getCategories()");
        Set<String> setCategories = SettingsStore.getSet(KEY_CATEGORIES, context);
        return Arrays.copyOf(setCategories.toArray(), setCategories.size(), String[].class);
    }
    public static boolean getDarkMode(Context context) {
        log("UserPrefs.getDarkMode(Context)");
        return SettingsStore.getBoolean(USE_DARK_MODE, false, context);
    }
    public static int getScrollPositionDayCalendar(Context context){
        return SettingsStore.getInt(KEY_SCROLL_POSITION_DAY_CALENDAR,0,  context);

    }
    public static boolean getShowAppointments(Context context) {
        return SettingsStore.getBoolean(KEY_SHOW_APPOINTMENTS, false, context);
    }
    public static boolean getShowDevScreen(Context context) {
        return SettingsStore.getBoolean(KEY_SHOW_DEV_SCREEN, false, context);
    }
    public static boolean getShowDuration(Context context) {
        return SettingsStore.getBoolean(KEY_SHOW_DURATION, false, context);
    }
    public static boolean getShowMedicine(Context context) {
        return SettingsStore.getBoolean(KEY_SHOW_MEDICINE, false, context);
    }
    public static boolean getShowMentalStatsScreen(Context context){
        return SettingsStore.getBoolean(KEY_SHOW_MENTAL_STATS_SCREEN, false, context);
    }
    public static boolean getShowTimeline(Context context) {
        return SettingsStore.getBoolean(KEY_SHOW_TIMELINE, false, context);
    }
    public static boolean getShowToDo(Context context) {
        return SettingsStore.getBoolean(KEY_SHOW_TODO, false, context);
    }
    public static boolean getSyncWithGoogleCalendar(Context context){
        return SettingsStore.getBoolean(SYNC_WITH_GOOGLE_CALENDAR, false, context);
    }

    public static String getLanguage(Context context) {
        return SettingsStore.getString(KEY_LANGUAGE, "sv", context);
    }
    public static SettingsStore.PanicAction getPanicAction(Context context) {
        return SettingsStore.PanicAction.valueOf(SettingsStore.getString(KEY_PANIC_ACTION, SettingsStore.PanicAction.SEQUENCE.toString(), context));
    }

    public static Set<String> getPanicUrls(Context context){
        return SettingsStore.getSet(KEY_PANIC_URLS, context);
    }
    public static String getPassword(Context context) {
        log("UserPrefs.getPassword()");
        return SettingsStore.getString(KEY_PASSWORD, "", context);
    }

    public static String getRandomPanicUrl(Context context){
        log("UserPrefs.getRandomPanicUrl(Context) ");
        List<String> urls = new ArrayList<>(getPanicUrls(context));
        log("...number of urls", urls.size());
        urls.forEach(System.out::println);
        Random random = new Random();
        return urls.get(random.nextInt(urls.size()));
    }
    public  static boolean getShowProjects(Context context) {
        log("UserPrefs.getShowProjects(Context)");
        return SettingsStore.getBoolean(KEY_SHOW_PROJECTS, false, context);

    }
    public static boolean isDevMode(Context context) {
        return SettingsStore.getBoolean(KEY_DEV_MODE, false, context);
    }
    public static void  setShowDevScreen(boolean show, Context context) {
        SettingsStore.addBoolean(KEY_DEV_MODE, show, context);

    }

    public static void setIcePhoneNumber(int phoneNumber, Context context){
        SettingsStore.addInt(KEY_ICE_PHONE_NUMBER, phoneNumber, context);
    }
    public static void setShowAppointments(boolean show, Context context){
        SettingsStore.addBoolean(KEY_SHOW_APPOINTMENTS, show, context);
    }
    public static void setShowDuration(boolean show, Context context){
        SettingsStore.addBoolean(KEY_SHOW_DURATION, show, context);
    }
    public static void setUsesPassword(boolean usesPassword, Context context){
        log("UserPrefs.setUsesPassword(boolean, Context)", usesPassword);
        SettingsStore.addBoolean(USES_PASSWORD, usesPassword, context);
    }
    public static boolean usesPassword(Context context) {
        if( VERBOSE) log("UserPrefs.usesPassword(Context)");
        return SettingsStore.getBoolean( USES_PASSWORD, false, context);
    }
    public static boolean validatePassword(String user, String pwd, Context context){
        log("...validatePassword(String, String)");
        String password = SettingsStore.getString(KEY_PASSWORD, "", context);
        return password.equals(pwd);
    }
    public static void savePassword(String pwd, Context context) {
        log("UserPrefs.savePassword(String, Context)");
        SettingsStore.addString(KEY_PASSWORD, pwd, context);
    }
    public static void setScrollPositionDayCalender(int position, Context context){
        SettingsStore.addInt(KEY_SCROLL_POSITION_DAY_CALENDAR, position, context);

    }
    public static void setShowMedicine(boolean show, Context context){
        SettingsStore.addBoolean(KEY_SHOW_MEDICINE, show, context);
    }
    public static void setShowTimeline(boolean show, Context context) {
        SettingsStore.addBoolean(KEY_SHOW_TIMELINE, show, context);
    }
    public static void setSyncWithGoogleCalendar(boolean syncWithGoogleCalendar, Context context) {
        SettingsStore.addBoolean(SYNC_WITH_GOOGLE_CALENDAR, syncWithGoogleCalendar, context);
    }
    public static void setUseDarkMode(boolean darkMode, Context context) {
        log("UserPrefs.setUseDarkMode(boolean, Context)");
        SettingsStore.addBoolean(USE_DARK_MODE, darkMode, context);
        if( darkMode){
            SettingsWorker.setDarkMode();
        }else{
            SettingsWorker.setLightMode();
        }
    }

    public static void setPanicUrls(String[] urlArray, Context context) {
        log("UserPrefs.setPanicUrls()");
        Set<String> urlSet = new HashSet<>(Arrays.asList(urlArray));
        SettingsStore.saveSet(KEY_PANIC_URLS,urlSet, context );
    }

    public static void setDevMode(boolean devMode, Context context){
        SettingsStore.addBoolean(KEY_DEV_MODE, devMode, context);
    }

    public static void setPanicAction(SettingsStore.PanicAction panicAction, Context context) {
        SettingsStore.addString(KEY_PANIC_ACTION, panicAction.toString(), context);
    }

    public static void setLanguage(String language, Context context) {
        log("UserPrefs.setLanguage(String, Context)");
        SettingsStore.addString(KEY_LANGUAGE, language , context);
        SettingsWorker.setLanguage(language);
    }

    public static void setPassword(String password, Context context) {
        log("UserPrefs.setPassword(String, Context)");
        SettingsStore.addString(KEY_PASSWORD, password, context);
    }

    public static void setCategories(String[] categories, Context context) {
        Set<String> urlSet = new HashSet<>(Arrays.asList(categories));
        SettingsStore.saveSet(KEY_CATEGORIES,urlSet, context );
    }

    public static void setShowProjects(boolean show, Context context){
        log("UserPrefs.setShowProjects(boolean, Context)", show);
        SettingsStore.addBoolean(KEY_SHOW_PROJECTS, show, context);
    }
    public static void setShowToDo(boolean show, Context context){
        SettingsStore.addBoolean(KEY_SHOW_TODO, show, context);

    }
    public static void setShowMentalStatsScreen(boolean show, Context context){
        SettingsStore.addBoolean(KEY_SHOW_MENTAL_STATS_SCREEN, show, context);
    }
    public static void deletePanicUrl(String url, Context context) {
        log("UserPrefs.deletePanicUrl(String)", url);
        Set<String> urls = UserPrefs.getPanicUrls(context);
        boolean stat = urls.remove(url);
        if( stat){
            UserPrefs.setPanicUrls(urls.toArray(new String[1]), context);
        }else{
            log("ERROR removing url", url);
        }
    }

    public static int getICE(Context context) {
        return  SettingsStore.getInt(KEY_ICE_PHONE_NUMBER, -1, context);
    }


    public static int getGoogleCalendarID(@NotNull Application context) {
        return SettingsStore.getInt(KEY_GOOGLE_CALENDAR_ID, -1, context);
    }

    public static void setGoogleCalendarID(int id, @NotNull Application context) {
        SettingsStore.addInt(KEY_GOOGLE_CALENDAR_ID, id, context);
    }

    public static InitialScreen getInitialScreen(@NotNull Application context) {
        return InitialScreen.values()[SettingsStore.getInt(KEY_INITIAL_SCREEN,InitialScreen.CALENDER_DATE.ordinal(), context)];
    }

    public static void setInitialScreen(InitialScreen initialScreen, @NotNull Application context) {
        SettingsStore.addInt(KEY_INITIAL_SCREEN, initialScreen.ordinal(), context);
    }

    public static boolean getShowMentalStatus(@NotNull Application context) {
        return SettingsStore.getBoolean(KEY_SHOW_MENTAL_STATUS, false, context);
    }

    public static void setShowMentalStatus(boolean value, @NotNull Application context) {
        SettingsStore.addBoolean(KEY_SHOW_MENTAL_STATUS, value, context);
    }

    @NotNull
    public static MentalFlag getMentalFlag(@NotNull Application context) {
        String json  = SettingsStore.getString(KEY_MENTAL_FLAG, "", context);
        if( json.isEmpty()){
            return new MentalFlag();
        }
        return new Gson().fromJson(json, MentalFlag.class);
    }

    public static void setMentalFlag(@NotNull MentalFlag mentalFlag, @NotNull Application context) {
        SettingsStore.addString(KEY_MENTAL_FLAG, new Gson().toJson(mentalFlag), context);
    }

    public static void setPanicOption(@NotNull PanicOption panicOption, @NotNull Application context) {
        SettingsStore.addString(KEY_PANIC_ACTION, panicOption.name(), context);
    }

    @NotNull
    public static PanicOption getPanicOption(@NotNull Application context) {
        log("UserPrefs.getPanicOption(Context)");
        String optionName = SettingsStore.getString(KEY_PANIC_ACTION, PanicOption.URL.name(), context);
        log("optionName", optionName);
        return PanicOption.valueOf(optionName);
    }
}
