package se.curtrune.lucy.screens.item_editor

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.services.TimerService
import se.curtrune.lucy.util.DateTImeConverter
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.features.notifications.NotificationsWorker

@Suppress("UNCHECKED_CAST")
class ItemEditorViewModel(val item: Item) : ViewModel() {
    private var elapsedTime = 0L
    private var _state = MutableStateFlow(ItemEditorState())
    val state = _state.asStateFlow()
    //private var _childItems = MutableLiveData<List<Item>>()
    private val _channel = Channel<ItemEditorChannel>()
    val channel = _channel.receiveAsFlow()
    private val repository = LucindaApplication.appModule.repository
    private val timeModule = LucindaApplication.appModule.timeModule
    private val mutableTimerState = MutableLiveData<TimerState>()
    private val mutableDuration = MutableLiveData<Long>()
    private val mutableError = MutableLiveData<String>()

    private var currentItem: Item? = null
    private val userSettings = LucindaApplication.appModule.userSettings
    var defaultItem: DefaultItemSettings = DefaultItemSettings()

    init{
        mutableTimerState.value = TimerState.PENDING
        _state.update { it.copy(
            categories = userSettings.categories,
            item = item
        ) }
        currentItem = item
    }
    companion object {
        fun factory(item: Item): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ItemEditorViewModel(item) as T
                }
            }
        }
        private const val MENTAL_OFFSET = 5
        var VERBOSE: Boolean = false
    }


    fun cancelNotification(context: Context?) {
        Logger.log("ItemSessionViewModel.cancelNotification(Context)")
        NotificationsWorker.cancelNotification(currentItem, context)
        currentItem!!.notification = null
        repository.update(currentItem!!)
    }

    private fun cancelTimer() {
        Logger.log("ItemSessionViewModel.cancelTimer()")
        timeModule.cancelTimer()
        mutableDuration.value = 0L
        mutableTimerState.value = TimerState.PENDING
    }



    val duration: LiveData<Long>
        get() = mutableDuration

    val error: LiveData<String>
        get() = mutableError
    val stress: Int
        get() = currentItem!!.stress
    val anxiety: Int
        get() = currentItem!!.anxiety
    val mood: Int
        get() = currentItem!!.mood
    val energy: Int
        get() = currentItem!!.energy

    enum class TimerState{
        PENDING, RUNNING, PAUSED,
    }



    val mental: Mental
        get() {
            Logger.log("ItemSessionViewModel.getMental()")
            return currentItem!!.mental
        }
    infix fun onEvent(event: ItemEvent){
        when(event){
            is ItemEvent.Update -> { update(event.item) }
            is ItemEvent.CancelTimer -> {cancelTimer()}
            is ItemEvent.PauseTimer -> {pauseTimer()}
            is ItemEvent.StartTimer -> {startTimer()}
            is ItemEvent.ResumeTimer -> {resumeTimer()}
            is ItemEvent.Delete -> { println("delete not implemented at this stage")}
            is ItemEvent.GetChildren -> {}
            is ItemEvent.GetItem -> {}
            is ItemEvent.GetChildrenType -> {}
            is ItemEvent.Edit -> {}
            is ItemEvent.InsertItem -> {}
            is ItemEvent.ShowAddItemDialog -> {
                println("ItemSessionViewModel.ShowAddItemDialog")
                showAddItemDialog()

            }
            is ItemEvent.InsertChild -> {
                insertChild(event.item)
            }

            is ItemEvent.AddCategory -> {
                addCategory(event.category)
            }
        }
    }
    private fun addCategory(category: String) {
        println("ItemSessionViewModel.addCategory($category)")
        userSettings.addCategory(category)
        _state.update { it.copy(
            categories = userSettings.categories
        ) }
    }
    private fun insertChild(child: Item){
        println("ItemSessionViewModel.insertChild(${item.heading})")
        currentItem?.let { repository.insertChild(it, child) }
        _state.update { it.copy(
            item = child
        ) }

    }

    private fun pauseTimer() {
        println("ItemSessionViewModel.pauseTimer()")
        timeModule.pauseTimer()
        mutableTimerState.value = TimerState.PAUSED
    }

    private fun resumeTimer() {
        println("ItemSessionViewModel.resumeTimer()")
        timeModule.resumeTimer()
        mutableTimerState.value = TimerState.RUNNING
        //timerRunning = true
        //runTimer()
    }

    fun setElapsedDuration(secs: Long) {
        println("ItemSessionViewModel.setElapsedDuration(${DateTImeConverter.formatSeconds(secs)})")
        elapsedTime = secs
    }
    private fun showAddItemDialog(){
        println("ItemSessionViewModel.showAddItemDialog()")
        val xxx = Item().also {
            it.category = currentItem!!.category
            it.parent = currentItem
        }
        defaultItem.item = xxx
        defaultItem.parent = currentItem
        viewModelScope.launch {
            _channel.send(ItemEditorChannel.ShowAddChildDialog)
        }
    }

    private fun startTimer() {
        Logger.log("ItemSessionViewModel.startTimer()")
        timeModule.startTimer(0)
        mutableTimerState.value = TimerState.RUNNING
        TimerService.currentDuration.observeForever { seconds->
            println("observing forever and ever $seconds")
            mutableDuration.value = seconds
        }

/*        currentTimerItem = currentItem
        timerRunning = true
        if(mutableTimerState.value == TimerState.PENDING){
            elapsedTime = 0
        }
        mutableTimerState.value = TimerState.RUNNING
        runTimer()*/
    }

    private fun update(item: Item): Boolean {
        Logger.log("ItemSessionViewModel.update(Item)", item.heading)
        val rowsAffected = repository.update(item)
        if (rowsAffected != 1) {
            mutableError.setValue("ERROR, updating item")
            viewModelScope.launch {
                _channel.send(ItemEditorChannel.ShowMessage("error updating item"))
            }
        } else {
            //ItemsWorker.touchParents(item, context)
            repository.touchParents(item)
        }
        return rowsAffected == 1
    }
}
