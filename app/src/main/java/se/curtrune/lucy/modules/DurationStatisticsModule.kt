package se.curtrune.lucy.modules

import android.app.Application
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import se.curtrune.lucy.app.User
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.Listable
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.persist.Queeries
import se.curtrune.lucy.screens.duration.DurationPeriod
import se.curtrune.lucy.statistics.CategoryListable
import se.curtrune.lucy.statistics.DateListable
import se.curtrune.lucy.util.Logger
import java.time.LocalDate
import java.util.function.Consumer
import java.util.stream.Collectors

class DurationStatisticsModule(private val context: Application) {
    var durationStatistics: DurationStatistics? = null
    var itemsByCategory: MutableList<Listable> = mutableListOf()
    var itemsByDate: MutableList<Listable> = mutableListOf()
    private var items: List<Item> = mutableListOf()
    private var firstDate: LocalDate? = null
    private var lastDate: LocalDate? = null
    private var currentPeriod: MutableStateFlow<DurationPeriod> = MutableStateFlow(DurationPeriod.DATE)
    init {
        initStatistics()
    }
    companion object{
        val VERBOSE = false
    }
    fun setPeriod(period: DurationPeriod){
        println("DurationStatisticsModule.setPeriod(${period})")
        currentPeriod.value = period
        setFirstAndLastDate(period)
        getItems(period)
        createCategoryListables(context)
        createDateListables()
    }
    private fun initStatistics(){
        durationStatistics = when(currentPeriod.value){
            DurationPeriod.DATE -> {
                DurationStatistics(LocalDate.now(), LocalDate.now(), context)
            }

            DurationPeriod.WEEK -> {
                DurationStatistics(LocalDate.now().minusWeeks(1), LocalDate.now(), context)
            }

            DurationPeriod.MONTH -> {
                DurationStatistics(LocalDate.now().minusMonths(1), LocalDate.now(), context)
            }

            DurationPeriod.YEAR -> {
                DurationStatistics(LocalDate.now().minusYears(1), LocalDate.now(), context)
            }
        }
    }
    private fun createCategoryListables(context: Context) {
        println("DurationStatisticsModule.createCategoryListables(Context))")
        val categories = User.getCategories(context)
        itemsByCategory = mutableListOf()
        //itemsByCategory.clear()
        for (category in categories) {
            val categoryItems =
                items.stream().filter { item: Item -> item.isCategory(category) }.collect(
                    Collectors.toList()
                )
            itemsByCategory.add(CategoryListable(category, categoryItems))
        }
    }
    private fun createDateListables() {
        println("DurationStatisticsModule.createDateListables()")
        itemsByDate.clear()
        var currentDate: LocalDate? = firstDate
        do {
            val finalCurrentDate = currentDate
            val dateItems =
                items.stream().filter { item: Item -> item.isUpdated(finalCurrentDate) }.collect(
                    Collectors.toList()
                )
            itemsByDate.add(DateListable(currentDate, dateItems))
            currentDate = currentDate?.plusDays(1)
        } while (!currentDate?.isAfter(lastDate)!!)
    }
    fun getDurationByCategory(period: DurationPeriod): DurationByCategory{
        val items = getItems(period)
        //createCategoryListables()
        return DurationByCategory(period)
    }
    private fun getItems(period: DurationPeriod){
        when(period){
            DurationPeriod.DATE -> getItems(LocalDate.now(), LocalDate.now(), context)
            DurationPeriod.WEEK -> getItems(LocalDate.now().minusDays(6), LocalDate.now(), context)
            DurationPeriod.MONTH -> getItems(LocalDate.now().minusMonths(1), LocalDate.now(), context)
            DurationPeriod.YEAR -> getItems(LocalDate.now().minusYears(1), LocalDate.now(), context)
        }
    }

    /**
     * gets items used for calculating stats
     * first and last date are inclusive
     */
    private fun getItems(firstDate: LocalDate, lastDate: LocalDate, context: Context) {
        println("DurationStatisticsModule.getItems($firstDate,$lastDate, Context)")
        LocalDB(context).use { db ->
            val queery = Queeries.selectItems(firstDate, lastDate, State.DONE)
            items = db.selectItems(queery)
            if (VERBOSE) {
                Logger.log("...items on parade")
                items.forEach(Consumer<Item> { item: Item? -> Logger.log(item) })
            }
        }
    }
    /**
     * help to convert periods to first and last date
     */
    private fun setFirstAndLastDate(period: DurationPeriod){
        when(period){
            DurationPeriod.DATE -> {
                firstDate = LocalDate.now()
                lastDate = LocalDate.now()
            }
            DurationPeriod.WEEK -> {
                lastDate = LocalDate.now()
                firstDate = lastDate!!.minusDays(6)
            }
            DurationPeriod.MONTH -> {
                lastDate = LocalDate.now()
                firstDate = lastDate!!.minusMonths(1).plusDays(1)
            }
            DurationPeriod.YEAR -> {
                lastDate = LocalDate.now()
                firstDate = lastDate!!.minusYears(1).plusDays(1)
            }
        }
    }
}