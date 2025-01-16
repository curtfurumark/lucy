package se.curtrune.lucy.screens.db_admin

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.persist.DBAdmin
import se.curtrune.lucy.persist.LocalDB

class DbAdminViewModel:  ViewModel() {
    private var _state = MutableStateFlow(DbAdminState())
    val state = _state.asStateFlow()
    //private lateinit var context: Context

    init {
        _state.value.dbName = LocalDB.getDbName()
        //DBAdmin.getTableNames(context)
        //LocalContext.current
    }
    fun test(){
    }
}