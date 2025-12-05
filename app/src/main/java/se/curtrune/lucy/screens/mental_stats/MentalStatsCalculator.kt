package se.curtrune.lucy.screens.mental_stats

import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

class MentalStatsCalculator(val items: List<Item>) {

    val sumAnxiety = items.stream().mapToInt(Item::anxiety).sum()
    val sumMood = items.stream().mapToInt(Item::mood).sum()
    val sumEnergy = items.stream().mapToInt(Item::energy).sum()
    //val sumJoy = items.stream().mapToInt(Item::getJoy).sum()
    //val sumHope = items.stream().mapToInt(Item::getHope).sum()
   // val sumHappiness = items.stream().mapToInt(Item::getHappiness).sum()
    //val sumLove = items.stream().mapToInt(Item::getLove).sum()
    val sumStress = items.stream().mapToInt(Item::stress).sum()
    private fun groupByDay(): Map<LocalDate, List<Item>> {
        return items.groupBy { it.targetDate }
    }
    fun getMentalDates(): List<MentalDate> {
        return groupByDay().map { (date, items) -> MentalDate(date, items) }.sortedByDescending { it.date }
    }
}


data class MentalDate(val date: LocalDate, val items: List<Item>){
    val energy = items.stream().mapToInt(Item::energy).sum()
    val mood = items.stream().mapToInt( Item::mood).sum()
    val anxiety = items.stream().mapToInt(Item::anxiety).sum()
    val stress = items.stream().mapToInt(Item::stress).sum()

}