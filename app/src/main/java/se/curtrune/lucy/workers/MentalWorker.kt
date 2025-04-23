package se.curtrune.lucy.workers

import android.content.Context
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.persist.Queeries
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.util.Logger
import java.time.LocalDate

object MentalWorker {
    var VERBOSE: Boolean = false
    val repository = LucindaApplication.appModule.repository

    /*** @param date the date you wish to examine
     * @param context, context
     * @return a Mental representing the sum of all the item said date
     */
    fun getMental(date: LocalDate, context: Context?): Mental {
        Logger.log("MentalWorker.getMental(LocalDate, Context)")
        val items: List<Item> = repository.selectItems(date)
        var anxiety = 0
        var energy = 0
        var mood = 0
        var stress = 0
        anxiety = items.stream().mapToInt { obj: Item -> obj.anxiety }.sum()
        energy = items.stream().mapToInt { obj: Item -> obj.energy }.sum()
        mood = items.stream().mapToInt { obj: Item -> obj.mood }.sum()
        stress = items.stream().mapToInt { obj: Item -> obj.stress }.sum()
        return Mental(anxiety, energy, mood, stress)
    }
    fun getMental(items: List<Item>): Mental {
        println("MentalWorker.getMental(List<Item>)")
        val mental = Mental()
        mental.anxiety = items.stream().mapToInt { obj: Item -> obj.anxiety }.sum()
        mental.energy = items.stream().mapToInt { obj: Item -> obj.energy }.sum()
        mental.mood = items.stream().mapToInt { obj: Item -> obj.mood }.sum()
        mental.stress = items.stream().mapToInt { obj: Item -> obj.stress }.sum()
        return mental
    }

    /**
     *
     * @param date the date to interrogate
     * @param context, context
     * @return Mental sum for done items
     */
    fun getCurrentMental(date: LocalDate, context: Context?): Mental {
        println("MentalWorker.getCurrentMental(LocalDate $date)")
        val queery = Queeries.selectItems(date, State.DONE)
        var items: List<Item>
        var anxiety = 0
        var energy = 0
        var mood = 0
        var stress = 0
        try {
            SqliteLocalDB(context).use { db ->
                items = db.selectItems(queery)
                anxiety = items.stream().mapToInt { obj: Item -> obj.anxiety }.sum()
                energy = items.stream().mapToInt { obj: Item -> obj.energy }.sum()
                mood = items.stream().mapToInt { obj: Item -> obj.mood }.sum()
                stress = items.stream().mapToInt { obj: Item -> obj.stress }.sum()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Logger.log(exception.message)
        }
        return Mental(anxiety, energy, mood, stress)
    }
}
