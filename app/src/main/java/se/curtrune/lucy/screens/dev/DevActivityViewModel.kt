package se.curtrune.lucy.screens.dev

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.screens.dev.test_cases.LocalDBTest
import se.curtrune.lucy.util.Logger

class DevActivityViewModel : ViewModel() {
    private var _mentalModule = LucindaApplication.mentalModule
    private var _mental = MutableStateFlow(_mentalModule.current)
    var mental = _mental.asStateFlow()
    private var _state = MutableStateFlow(DevState())
    val state = _state.asStateFlow()

    init{
        println("....initBlock")
        _state.value.systemInfoList = LucindaApplication.systemInfoModule.systemInfo
        println("...number of sys infos: ${_state.value.systemInfoList.size}")
        _state.value.mental = LucindaApplication.mentalModule.current.value
    }

    fun listColumns(context: Context?) {
        Logger.log("DevActivity.listColumns(Context)")
        LocalDB(context).use { db ->
            db.getColumns("items")
        }
    }
    fun onEvent(event: DevEvent){
        println("...onEvent(${event.toString()})")
        when(event){
            DevEvent.CreateItemTree -> LocalDBTest().createTreeToDelete()
        }

    }
}
