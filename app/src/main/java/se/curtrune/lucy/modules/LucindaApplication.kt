package se.curtrune.lucy.modules

import android.app.Application
import se.curtrune.lucy.app.Settings
import se.curtrune.lucy.features.google_calendar.GoogleCalendarModule
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.persist.Repository
import se.curtrune.lucy.features.TimeModule
import se.curtrune.lucy.statistics.Statistics
import java.time.LocalDate

class LucindaApplication: Application() {
    companion object{
        lateinit var appModule: AppModule
        lateinit var contextModule: ContextModule
        //lateinit var localDB: SqliteLocalDB
        //lateinit var repository: Repository
        //lateinit var statistics: Statistics
        //lateinit var settings: Settings
        //lateinit var timeModule: TimeModule
    }

    override fun onCreate() {
        super.onCreate()
        println("onCreate of LucindaApplication")
        appModule = AppModuleImpl(this)
        //repository = Repository(this)
        contextModule = ContextModule(this)
        //timeModule = TimeModule(this)
        //localDB  =  SqliteLocalDB(this)
        //statistics = Statistics(repository.selectItems(LocalDate.now()))
        //settings = Settings.getInstance(this)
    }
}