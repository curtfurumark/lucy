package se.curtrune.lucy

import android.app.Application
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.modules.CalendarModule
import se.curtrune.lucy.modules.MentalModule
import se.curtrune.lucy.modules.SystemInfoModule
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.modules.Repository
import se.curtrune.lucy.modules.TimeModule
import se.curtrune.lucy.modules.UserSettings
import se.curtrune.lucy.statistics.Statistics
import java.time.LocalDate

class LucindaApplication: Application() {
    companion object{
        lateinit var mentalModule: MentalModule
        lateinit var localDB: SqliteLocalDB
        lateinit var systemInfoModule: SystemInfoModule
        lateinit var repository: Repository
        lateinit var statistics: Statistics
        lateinit var settings: Settings
        lateinit var timeModule: TimeModule
        lateinit var userSettings: UserSettings
        lateinit var calendarModule: CalendarModule
    }

    override fun onCreate() {
        super.onCreate()
        timeModule = TimeModule(this)
        mentalModule = MentalModule(this)
        localDB = SqliteLocalDB(this)
        systemInfoModule = SystemInfoModule(this)
        repository = Repository(this)
        statistics = Statistics(repository.selectItems(LocalDate.now()))
        settings = Settings.getInstance(this)
        userSettings = UserSettings(this)
    }
}