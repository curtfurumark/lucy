package se.curtrune.lucy.screens.duration

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private var _state =  MutableStateFlow(DurationState())
    val state = _state.asStateFlow()
    private var durationModule: DurationStatisticsModule? = null

    init{
        println("init block")
        durationModule = LucindaApplication.durationStatistics
        durationModule!!.setPeriod(DurationPeriod.DATE)
        _state.value.items = durationModule!!.itemsByCategory
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
        durationModule!!.setPeriod(period)
        val categorizedItems = durationModule!!.itemsByCategory
        println("...got ${categorizedItems.size} categorized items")
        printCategoryListables(categorizedItems)
        _state.value.items = categorizedItems
/*        _state.update { it.copy(
            items = categorizedItems
        )
        }*/
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
