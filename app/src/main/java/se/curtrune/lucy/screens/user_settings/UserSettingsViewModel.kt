package se.curtrune.lucy.screens.user_settings

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.app.UserPrefs

class UserSettingsViewModel(private val context: Context): ViewModel(){
    var isDarkMode = mutableStateOf(false)
        set(value){
            println("...setting dark mode to $value")
            field = value
            UserPrefs.setUseDarkMode(field.value, context)
        }
    var categories = emptyList<String>()
    init {
        isDarkMode.value = UserPrefs.getDarkMode(context)
        categories = UserPrefs.getCategories(context).toList()
    }
    fun getLanguage(): String{
        return UserPrefs.getLanguage(context)
    }
    fun setLanguage(language: String){
        UserPrefs.setLanguage(language, context)
    }

    fun getPanicUrls(): Array<String> {
        return UserPrefs.getPanicUrls(context).toTypedArray()
    }

    fun isDarkMode(context: Context): Boolean {
        return UserPrefs.getDarkMode(context)
    }
    fun setDarkMode(darkMode: Boolean){
        UserPrefs.setUseDarkMode(darkMode, context)
    }

}