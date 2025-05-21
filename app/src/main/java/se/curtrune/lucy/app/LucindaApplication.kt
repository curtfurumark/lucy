package se.curtrune.lucy.app

import android.app.Application
import se.curtrune.lucy.modules.ContextModule
import se.curtrune.lucy.modules.TopAppbarModule

class LucindaApplication: Application() {
    companion object{
        lateinit var appModule: AppModule
        lateinit var contextModule: ContextModule
    }

    override fun onCreate() {
        super.onCreate()
        println("onCreate of LucindaApplication")
        appModule = AppModuleImpl(this)
        contextModule = ContextModule(this)
        initUserSettings()
    }
    private fun initUserSettings(){
        println("LucindaApplication.initUserSettings()")
        val userSettings = appModule.userSettings
        val showMentalStatus = userSettings.showMentalStatus
        println("showMentalStatus: $showMentalStatus")
        TopAppbarModule.setShowMental(showMentalStatus)

    }
}