package se.curtrune.lucy

import org.junit.Assert.*
import org.junit.Test
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.statistics.Statistics

class StatisticsTest {

    @Test
    fun testDuration(){
        println("testDuration()")
        val durations : List<Long> = listOf(15, 15, 15, 10)
        val items = getDurationItems(durations)
        val statistics = Statistics(items)
        assertEquals(55, statistics.duration)
    }
    @Test
    fun testEnergy(){
        println("testEnergy()")
        val statistics = Statistics(getEnergyItems(listOf(5, 4, -3, 0, - 3)))
        assertEquals(3, statistics.energy)

    }
    @Test
    fun testAverageEnergy(){
        println("testEnergy()")
        val statistics = Statistics(getEnergyItems(listOf(5, 3, 2, 0)))
        assertEquals(2.5f, statistics.averageEnergy)
    }
    @Test
    fun testEmptyListEnergy(){
        println("testEmptyListEnergy()")
        val statistics = Statistics(getEnergyItems(emptyList()))
        assertEquals(0, statistics.energy)
    }
    @Test
    fun testEmptyListAverageEnergy(){
        println("testEmptyListAverageEnergy()<")
        val statistics = Statistics(getEnergyItems(emptyList()))
        assertEquals(0f, statistics.averageEnergy)

    }
    private fun getDurationItems(durations: List<Long>): List<Item>{
        val itemList =  arrayListOf(Item())
        durations.forEach{ duration->
            val item = Item("duration: $duration")
            item.duration = duration
            itemList.add(item)
        }
        return itemList
    }
    private fun getEnergyItems(energyList: List<Int>): List<Item>{
        val itemList: MutableList<Item> =  mutableListOf()
        energyList.forEach{ energy->
            val item = Item("energy: $energy")
            item.energy = energy
            itemList.add(item)
        }
        return itemList
    }

    @Test
    fun testByCategory(){
        println("test by category")

    }
}