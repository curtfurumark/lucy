package se.curtrune.lucy.screens.db_admin

import android.content.Context
import se.curtrune.lucy.persist.SqliteLocalDB

class DataBaseAdmin(val context: Context) {
    fun getDBPath():String{
        var file = context.getDatabasePath(SqliteLocalDB.dbName)
        return file.absolutePath
    }
}