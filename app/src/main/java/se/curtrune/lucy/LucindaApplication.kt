package se.curtrune.lucy

import android.app.Application
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.features.google_calendar.CalendarModule
import se.curtrune.lucy.modules.ContextModule
import se.curtrune.lucy.modules.MentalModule
import se.curtrune.lucy.modules.SystemInfoModule
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.persist.Repository
import se.curtrune.lucy.features.TimeModule
import se.curtrune.lucy.modules.UserSettings
import se.curtrune.lucy.statistics.Statistics
import se.curtrune.lucy.workers.InternetWorker
import java.time.LocalDate

class LucindaApplication: Application() {
    companion object{
        lateinit var mentalModule: MentalModule
        lateinit var contextModule: ContextModule
        lateinit var localDB: SqliteLocalDB
        lateinit var systemInfoModule: SystemInfoModule
        lateinit var repository: Repository
        lateinit var statistics: Statistics
        lateinit var settings: Settings
        lateinit var timeModule: TimeModule
        lateinit var userSettings: UserSettings
        lateinit var calendarModule: CalendarModule
        lateinit var internetWorker: InternetWorker
    }

    override fun onCreate() {
        super.onCreate()
        println("onCreate of LucindaApplication")
        systemInfoModule = SystemInfoModule(this)
        systemInfoModule.systemInfo.forEach{
            println("${it.key}: ${it.value}")
        }
        repository = Repository(this)
        contextModule = ContextModule(this)
        calendarModule = CalendarModule(this)
        timeModule = TimeModule(this)
        mentalModule = MentalModule(this)
        localDB  =  SqliteLocalDB(this)
        statistics = Statistics(repository.selectItems(LocalDate.now()))
        settings = Settings.getInstance(this)
        userSettings = UserSettings(this)
        internetWorker =   InternetWorker(this)
    }
}