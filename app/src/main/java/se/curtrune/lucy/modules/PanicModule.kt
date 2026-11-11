package se.curtrune.lucy.modules

import android.app.Application
import se.curtrune.lucy.app.LucindaApplication

class PanicModule(private val application: Application) {
    private val userSettings = LucindaApplication.appModule.userSettings
    fun getPreferredPanicAction(){
        val panicOption = userSettings.panicOption
    }
}