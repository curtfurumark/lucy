package se.curtrune.lucy.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.app.User

class UserSettingsViewModel: ViewModel(){
    fun getLanguage(context: Context): String{
        return User.getLanguage(context)
    }
    fun setLanguage(language: String, context: Context){
        User.setLanguage(language, context)
    }

    fun getPanicUrls(context: Context): Array<String> {
        return User.getPanicUrls(context).toTypedArray()
    }

    fun isDarkMode(context: Context): Boolean {
        return User.getDarkMode(context)
    }

}