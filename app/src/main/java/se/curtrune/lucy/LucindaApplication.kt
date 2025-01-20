package se.curtrune.lucy

import android.app.Application
import se.curtrune.lucy.modules.MentalModule
import se.curtrune.lucy.modules.SystemInfoModule
import se.curtrune.lucy.persist.LocalDB

class LucindaApplication: Application() {
    companion object{
        lateinit var mentalModule: MentalModule
        lateinit var localDB: LocalDB
        lateinit var systemInfoModule: SystemInfoModule
    }

    override fun onCreate() {
        super.onCreate()
        mentalModule = MentalModule(this)
        localDB = LocalDB(this)
        systemInfoModule = SystemInfoModule(this)
    }
}