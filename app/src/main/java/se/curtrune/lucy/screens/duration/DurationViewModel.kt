package se.curtrune.lucy.screens.duration

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Listable
import se.curtrune.lucy.modules.DurationStatistics
import se.curtrune.lucy.modules.DurationStatisticsModule
import se.curtrune.lucy.screens.util.Converter
import se.curtrune.lucy.statistics.CategoryListable
import se.curtrune.lucy.util.Logger
import java.time.LocalDate

class DurationViewModel : ViewModel() {
    //private val _state =  mutableStateOf(DurationState())
    private val _state = MutableStateFlow(DurationState())
    //private val _state: MutableSharedFlow<DurationState> = MutableSharedFlow()
    val state = _state
    private var durationModule: DurationStatisticsModule? = null

    init{
        println("init block")
        durationModule = LucindaApplication.durationStatistics
        durationModule!!.setPeriod(DurationPeriod.DATE)
        _state.value = state.value.copy(
            items = durationModule!!.itemsByCategory,
            message = "hello from init"
        )
    }
/*    fun set(firstDate: LocalDate?, lastDate: LocalDate?, context: Context?) {
        Logger.log("DurationViewModel.set(LocalDate, LocalDate, Context)")
        durationModule?.setPeriod(DurationPeriod.DATE)
        _state.value.items = durationModule!!.itemsByCategory
    }*/

    fun onEvent(event: DurationEvent){
        when(event){
            is DurationEvent.SetPeriod -> { setPeriod(event.period)}
        }
    }
    private fun setPeriod(period: DurationPeriod){
        println("DurationViewModel.setPeriod(${period.toString()})")
        _state.value = state.value.copy(
            showProgressBar = true,
            message = period.name
        )
        durationModule!!.setPeriod(period)
        val categorizedItems = durationModule!!.itemsByCategory
        //categorizedItems.sort {  item  ->item.compare()  }
        categorizedItems.sortBy{item ->item.compare()}
        println("...got ${categorizedItems.size} categorized items")
        printCategoryListables(categorizedItems)
        _state.value = state.value.copy(
            showProgressBar = false
        )
    }
}

fun printCategoryListables(items: MutableList<Listable>){
    items.forEach{ item->
        printlnCategoryItem(item as CategoryListable)
    }
}

fun printlnCategoryItem(item: CategoryListable){
    println(item.heading)
    println(Converter.formatSecondsWithHours(item.duration))
}
