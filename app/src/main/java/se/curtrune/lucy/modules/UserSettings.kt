package se.curtrune.lucy.modules

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.app.UserPrefs
import se.curtrune.lucy.screens.navigation.NavigationDrawerState
import se.curtrune.lucy.screens.settings.MentalFlag
import se.curtrune.lucy.screens.settings.PanicOption
import se.curtrune.lucy.util.Logger
import java.util.Arrays

class UserSettings(val context: Application) {
    val navigationDrawerState = MutableStateFlow(NavigationDrawerState().also {
        it.showProjectsLink = showProjects
        it.showDurationLink = showDuration
    })

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

    fun deleteCategory(category: String) {
        //Logger.log("UserSettings.deleteCategory($category)")
        val setCategories = Settings.getSet(UserPrefs.KEY_CATEGORIES, context)
        setCategories.remove(category)
        Settings.saveSet(UserPrefs.KEY_CATEGORIES, setCategories, context)


    }

    fun addCategory(category: String) {
        val setCategories = Settings.getSet(UserPrefs.KEY_CATEGORIES, context)
        setCategories.add(category)
        Settings.saveSet(UserPrefs.KEY_CATEGORIES, setCategories, context)
    }

    var panicOption: PanicOption
        get() = UserPrefs.getPanicOption(context)
        set(value){
            UserPrefs.setPanicOption(value, context)
        }
    val categories: List<String>
        get() = Settings.getList(UserPrefs.KEY_CATEGORIES, context)

    var darkMode: Boolean
        get() = UserPrefs.getDarkMode(context)
        set(value){
            UserPrefs.setUseDarkMode(value, context)
            //field = value
        }
    var initialScreen: InitialScreen
        get() = UserPrefs.getInitialScreen(context)
        set(value){
            UserPrefs.setInitialScreen(value, context)
        }

    var password: String
        get() = UserPrefs.getPassword(context)
        set(value){
            UserPrefs.setPassword(value, context)
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

    var syncWithGoogleCalendar: Boolean
        get() = UserPrefs.getSyncWithGoogleCalendar(context)
        set(value){
            UserPrefs.setSyncWithGoogleCalendar(value, context)
        }
/*    var showDuration: Boolean
        get() = UserPrefs.getShowDuration(context)
        set(value){
            UserPrefs.setShowDuration(value, context)
        }*/
    var showMedicine: Boolean
        get() = UserPrefs.getShowMedicine(context)
        set(value){
            UserPrefs.setShowMedicine(value, context)
        }
    var showProjects: Boolean
        get() = UserPrefs.getShowProjects(context)
        set(value){
            UserPrefs.setShowProjects(value, context)
            navigationDrawerState.update {
                it.copy(showProjectsLink = value)
            }
        }
    var showDuration: Boolean
    get() = UserPrefs.getShowDuration(context)
        set(value){
            UserPrefs.setShowDuration(value, context)
        }
/*    var showAppointments: Boolean
        get() = UserPrefs.getShowAppointments(context)
        set(value){
        }*/
    var usesPassword: Boolean
        get() = UserPrefs.usesPassword(context)
        set(value){
            UserPrefs.setUsesPassword(value, context)
        }
    var showMentalStatus: Boolean
        get() = UserPrefs.getShowMentalStatus(context)
        set(value){
            UserPrefs.setShowMentalStatus(value, context)
        }
    var mentalFlag: MentalFlag
        get() = UserPrefs.getMentalFlag(context)
        set(value){
            UserPrefs.setMentalFlag(value, context)
        }
}