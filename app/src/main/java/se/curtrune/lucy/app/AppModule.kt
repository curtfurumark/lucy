package se.curtrune.lucy.app

import android.app.Application
import se.curtrune.lucy.features.TimeModule
import se.curtrune.lucy.features.google_calendar.GoogleCalendarModule
import se.curtrune.lucy.modules.ContactsModule
import se.curtrune.lucy.modules.MentalModule
import se.curtrune.lucy.modules.SystemInfoModule
import se.curtrune.lucy.modules.UserSettings
import se.curtrune.lucy.persist.Repository
import se.curtrune.lucy.persist.SettingsStore
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.workers.InternetWorker

interface AppModule{
    val repository: Repository
    val mentalModule: MentalModule
    val userSettings: UserSettings
    val internetWorker: InternetWorker
    val googleCalendarModule: GoogleCalendarModule
    val systemInfoModule: SystemInfoModule
    val settings: SettingsStore
    val timeModule: TimeModule
    val sqliteLocalDB: SqliteLocalDB
    val contactsModule: ContactsModule
}
class AppModuleImpl(private val context: Application): AppModule {
    override val repository: Repository by lazy {
        Repository(context)
    }
    override val mentalModule: MentalModule by lazy {
        MentalModule(context)
    }
    override val userSettings: UserSettings by lazy {
        UserSettings(context)
    }
    override val internetWorker: InternetWorker by lazy {
        InternetWorker(context)
    }
    override val googleCalendarModule: GoogleCalendarModule by lazy {
        GoogleCalendarModule(context)
    }
    override val systemInfoModule: SystemInfoModule by lazy {
        SystemInfoModule(context)
    }
    override val settings: SettingsStore by lazy {
        SettingsStore.getInstance(context)
    }
    override val timeModule: TimeModule by lazy {
        TimeModule(context)
    }
    override val sqliteLocalDB: SqliteLocalDB by lazy {
        SqliteLocalDB(context)
    }
    override val contactsModule: ContactsModule by lazy {
        ContactsModule(context)
    }
}