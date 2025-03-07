package se.curtrune.lucy.modules

import android.app.Application
import android.content.Context
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.util.Logger
import java.util.Arrays

class UserSettings(val context: Application) {

    /**
     * list of categories, stored in sharedPreferences, settings whatever you want to call it
     * @param context, context, whatever that is
     * @return an array of categories
     */
    fun getCategories(): Array<String> {
        Logger.log("UserSettings.getCategories()")
        val setCategories = Settings.getSet(UserPrefs.KEY_CATEGORIES, context)
        return Arrays.copyOf<String, Any>(
            setCategories.toTypedArray(), setCategories.size,
            Array<String>::class.java
        )
    }

    var language: String
        get() = UserPrefs.getLanguage(context)
        set(value){
            UserPrefs.setLanguage(value, context)
        }
    var googleCalendarId: Int
        get() = UserPrefs.getGoogleCalendarID(context)
        set(value){
            UserPrefs.setGoogleCalendarID(value, context)
        }
    var darkMode: Boolean
        get() = UserPrefs.getDarkMode(context)
        set(value){
            UserPrefs.setUseDarkMode(value, context)
            //field = value
        }
    var syncWithGoogleCalendar: Boolean
        get() = UserPrefs.getSyncWithGoogleCalendar(context)
        set(value){
            UserPrefs.setSyncWithGoogleCalendar(value, context)
        }
}