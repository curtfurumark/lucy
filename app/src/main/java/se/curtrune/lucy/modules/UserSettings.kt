package se.curtrune.lucy.modules

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.InitialScreen
import se.curtrune.lucy.persist.SettingsStore
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
        val setCategories = SettingsStore.getSet(UserPrefs.KEY_CATEGORIES, context)
        return Arrays.copyOf<String, Any>(
            setCategories.toTypedArray(), setCategories.size,
            Array<String>::class.java
        )
    }

    fun deleteCategory(category: String) {
        //Logger.log("UserSettings.deleteCategory($category)")
        val setCategories = SettingsStore.getSet(UserPrefs.KEY_CATEGORIES, context)
        setCategories.remove(category)
        SettingsStore.saveSet(UserPrefs.KEY_CATEGORIES, setCategories, context)


    }

    fun addCategory(category: String) {
        val setCategories = SettingsStore.getSet(UserPrefs.KEY_CATEGORIES, context)
        setCategories.add(category)
        SettingsStore.saveSet(UserPrefs.KEY_CATEGORIES, setCategories, context)
    }

    var panicOption: PanicOption
        get() = UserPrefs.getPanicOption(context)
        set(value){
            UserPrefs.setPanicOption(value, context)
        }
    val categories: List<String>
        get() = SettingsStore.getList(UserPrefs.KEY_CATEGORIES, context)

    var darkMode: Boolean
        get() = UserPrefs.getDarkMode(context)
        set(value){
            UserPrefs.setUseDarkMode(value, context)
        }
    var devMode: Boolean
        get() = UserPrefs.isDevMode(context)
        set(value){
            UserPrefs.setDevMode(value, context)
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
    var showAppointmentsLink: Boolean
        get() = UserPrefs.getShowAppointments(context)
        set(value){
            UserPrefs.setShowAppointments(value, context)
        }

    var showDevScreen: Boolean
        get() = UserPrefs.getShowDevScreen(context)
        set(value){
            UserPrefs.setShowDevScreen(value, context)
        }
    var showMedicine: Boolean
        get() = UserPrefs.getShowMedicine(context)
        set(value){
            UserPrefs.setShowMedicine(value, context)
        }
    var showMentalStatsScreen: Boolean
        get() = UserPrefs.getShowMentalStatsScreen(context)
        set(value){
            UserPrefs.setShowMentalStatsScreen(value, context)
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
    var showTimeLine: Boolean
        get() = UserPrefs.getShowTimeline(context)
        set(value){
            UserPrefs.setShowTimeline(value, context)
        }
    var showToDo: Boolean
        get() = UserPrefs.getShowToDo(context)
        set(value){
            UserPrefs.setShowToDo(value, context)
        }
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