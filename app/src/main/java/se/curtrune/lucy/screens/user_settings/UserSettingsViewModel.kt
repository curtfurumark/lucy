package se.curtrune.lucy.screens.user_settings

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.app.User

class UserSettingsViewModel(private val context: Context): ViewModel(){
    var isDarkMode = mutableStateOf(false)
        set(value){
            println("...setting dark mode to $value")
            field = value
            User.setUseDarkMode(field.value, context)
        }
    var categories = emptyList<String>()
    init {
        isDarkMode.value = User.getDarkMode(context)
        categories = User.getCategories(context).toList()
    }
    fun getLanguage(): String{
        return User.getLanguage(context)
    }
    fun setLanguage(language: String){
        User.setLanguage(language, context)
    }

    fun getPanicUrls(): Array<String> {
        return User.getPanicUrls(context).toTypedArray()
    }

    fun isDarkMode(context: Context): Boolean {
        return User.getDarkMode(context)
    }
    fun setDarkMode(darkMode: Boolean){
        User.setUseDarkMode(darkMode, context)
    }

}