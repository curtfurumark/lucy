package se.curtrune.lucy.screens.db_admin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.persist.SqliteLocalDB

class DbAdminViewModel:  ViewModel() {
    private var _state = MutableStateFlow(DbAdminState())
    val state = _state.asStateFlow()
    //private lateinit var context: Context

    init {
        _state.value.dbName = SqliteLocalDB.getDbName()
        //DBAdmin.getTableNames(context)
        //LocalContext.current
    }
    fun test(){
    }
}