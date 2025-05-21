package se.curtrune.lucy.screens.item_editor

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.item_settings.CheckBoxSetting
import se.curtrune.lucy.item_settings.ItemSetting
import se.curtrune.lucy.item_settings.KeyValueSetting
import se.curtrune.lucy.services.TimerService
import se.curtrune.lucy.util.DateTImeConverter
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.features.notifications.NotificationsWorker

class ItemSessionViewModel : ViewModel() {
    private var elapsedTime = 0L
    private val repository = LucindaApplication.appModule.repository
    private val timeModule = LucindaApplication.appModule.timeModule
    private val mutableTimerState = MutableLiveData<TimerState>()
    private val mutableDuration = MutableLiveData<Long>()
    private val mutableError = MutableLiveData<String>()
    private val mutableCurrentItem = MutableLiveData<Item?>()
    private var currentItem: Item? = null

    init{
        mutableTimerState.value = TimerState.PENDING
    }
    fun addTags(tags: String?, context: Context?) {
        currentItem!!.tags = tags
        //update(currentItem!!, context)
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

    fun init(item: Item, context: Context?) {
        Logger.log("ItemSessionViewModel.init(Item)")
        this.currentItem = item
        if (currentItem!!.repeatID > 0) {
            val repeat =    repository.selectRepeat(currentItem!!.repeatID)
            currentItem!!.setRepeat(repeat)
        }
        mutableCurrentItem.value = currentItem
        mutableDuration.value = currentItem!!.duration
    }

    fun getCurrentItem(): MutableLiveData<Item?> {
        return mutableCurrentItem
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

    fun getItemSettings(item: Item, context: Context): List<ItemSetting> {
        checkNotNull(item)
        Logger.log("ItemEditorViewModel.getItemSettings(Item)")
        val settings: MutableList<ItemSetting> = ArrayList()
        val dateSetting = KeyValueSetting(
            context.getString(R.string.date),
            item.targetDate.toString(),
            ItemSetting.Key.DATE
        )
        val timeSetting = KeyValueSetting(
            context.getString(R.string.time),
            item.targetTime.toString(),
            ItemSetting.Key.TIME
        )
        var repeatValue = ""
        if (item.hasRepeat()) {
            repeatValue = item.repeat.toString()
        }
        val repeatSetting =
            KeyValueSetting(context.getString(R.string.repeat), repeatValue, ItemSetting.Key.REPEAT)
        val colourSetting = KeyValueSetting(
            context.getString(R.string.color),
            item.color.toString(),
            ItemSetting.Key.COLOR
        )
        val templateSetting = CheckBoxSetting(
            context.getString(R.string.is_template),
            item.isTemplate,
            ItemSetting.Key.TEMPLATE
        )
        val prioritizedSetting = CheckBoxSetting(
            context.getString(R.string.is_prioritized),
            item.isPrioritized,
            ItemSetting.Key.PRIORITIZED
        )
        val durationSetting = KeyValueSetting(
            context.getString(R.string.duration),
            DateTImeConverter.formatSeconds(item.duration),
            ItemSetting.Key.DURATION
        )
        var notificationValue = ""

        if (item.hasNotification()) {
            notificationValue = item.notification.toString()
        }
        val notificationSetting =
            KeyValueSetting("notification", notificationValue, ItemSetting.Key.NOTIFICATION)
        val tagsSetting =
            KeyValueSetting(context.getString(R.string.tags), item.tags, ItemSetting.Key.TAGS)
        val calenderSetting = CheckBoxSetting(
            context.getString(R.string.is_calender),
            item.isCalenderItem,
            ItemSetting.Key.IS_CALENDAR_ITEM
        )
        val categorySetting = KeyValueSetting(
            context.getString(R.string.category),
            item.category,
            ItemSetting.Key.CATEGORY
        )

        settings.add(dateSetting)
        settings.add(timeSetting)
        settings.add(repeatSetting)
        settings.add(categorySetting)
        settings.add(calenderSetting)
        settings.add(notificationSetting)
        settings.add(durationSetting)
        settings.add(prioritizedSetting)
        settings.add(templateSetting)
        settings.add(tagsSetting)
        settings.add(colourSetting)
        return settings
    }
    val item: Item?
        get() {
            Logger.log("ItemSessionViewModel.getItem()")
            return currentItem
        }

    fun itemHasRepeat(): Boolean {
        Logger.log("ItemSessionViewModel.itemHasRepeat(Context)")
        return currentItem!!.repeatID > 0
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
            is ItemEvent.ShowAddItemDialog -> {}
            is ItemEvent.InsertChild -> {
                insertChild(event.item)
            }
        }
    }
    private fun insertChild(item: Item){
        println("ItemSessionViewModel.insertChild(${item.heading})")
        currentItem?.let { repository.insertChild(it, item) }

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
        } else {
            //ItemsWorker.touchParents(item, context)
            repository.touchParents(item)
        }
        return rowsAffected == 1
    }

    fun update(itemSetting: ItemSetting, item: Item, context: Context?) = when (itemSetting.key) {
        ItemSetting.Key.DATE -> {
            itemSetting.value =
                item.targetDate.toString()
        }

        ItemSetting.Key.TIME -> {
            TODO()
        }
        ItemSetting.Key.DURATION -> {
            TODO()
        }
        ItemSetting.Key.COLOR -> {
            TODO()
        }
        ItemSetting.Key.REPEAT -> {
            TODO()
        }
        ItemSetting.Key.NOTIFICATION -> {
            TODO()
        }
        ItemSetting.Key.CATEGORY -> {
            TODO()
        }
        ItemSetting.Key.TAGS -> {
            TODO()
        }
        ItemSetting.Key.APPOINTMENT -> {
            TODO()
        }
        ItemSetting.Key.DONE -> {
            TODO()
        }
        ItemSetting.Key.TEMPLATE -> {
            TODO()
        }
        ItemSetting.Key.PRIORITIZED -> {
            TODO()
        }
        ItemSetting.Key.IS_CALENDAR_ITEM -> {
            TODO()
        }
        else ->{
            println("ELSE ERROR, itemSessionViewModel")
        }
    }

    val timerState: LiveData<TimerState>
        get() {
            println("ItemSessionViewModel.getTimerState()")
            return mutableTimerState
        }


    fun updateNotification(context: Context?) {
        Logger.log("ItemSessionViewModel.updateNotification(Context)")
    }

    companion object {
        private const val MENTAL_OFFSET = 5
        var VERBOSE: Boolean = false
    }
}
