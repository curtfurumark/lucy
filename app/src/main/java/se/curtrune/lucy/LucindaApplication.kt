package se.curtrune.lucy

import android.app.Application
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.modules.MentalModule
import se.curtrune.lucy.modules.SystemInfoModule
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.modules.Repository
import se.curtrune.lucy.modules.TimeModule
import se.curtrune.lucy.statistics.Statistics
import java.time.LocalDate

class LucindaApplication: Application() {
    companion object{
        lateinit var mentalModule: MentalModule
        lateinit var localDB: LocalDB
        lateinit var systemInfoModule: SystemInfoModule
        lateinit var repository: Repository
        lateinit var statistics: Statistics
        lateinit var settings: Settings
        lateinit var timeModule: TimeModule
    }

    override fun onCreate() {
        super.onCreate()
        timeModule = TimeModule(this)
        mentalModule = MentalModule(this)
        localDB = LocalDB(this)
        systemInfoModule = SystemInfoModule(this)
        repository = Repository(this)
        statistics = Statistics(repository.selectItems(LocalDate.now()))
        settings = Settings.getInstance(this)
    }
}