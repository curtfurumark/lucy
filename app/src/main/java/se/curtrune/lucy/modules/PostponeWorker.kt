package se.curtrune.lucy.modules

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.dialogs.PostponeAmount

object PostponeWorker {
    fun postponeItem(item: Item, amount: PostponeAmount): Item {
        when(amount){
            PostponeAmount.ONE_HOUR ->  postponeItemMinutes(item, 60)
            PostponeAmount.TWO_HOURS -> postponeItemMinutes(item, 120)
            PostponeAmount.ONE_DAY -> item.targetDate = item.targetDate.plusDays(1)
            PostponeAmount.ONE_WEEK -> item.targetDate = item.targetDate.plusWeeks(1)
            PostponeAmount.ONE_MONTH -> item.targetDate = item.targetDate.plusMonths(1)
        }
        return item
    }
    private fun postponeItemMinutes(item: Item, minutes: Long): Item {
        val oldTargetTime = item.targetTime
        val newTargetTime = item.targetTime.plusMinutes(minutes)
        if( newTargetTime.isBefore(oldTargetTime)){//overflow to next day,
            item.targetDate = item.targetDate.plusDays(1)
        }
        item.targetTime = newTargetTime
        return item

    }
    //fun
/*    fun postponeAll(details: PostponeDetails, items: List<Item>) {
        println("postponeAll(postponeDetails)")
        val filteredItems = items.filter { item -> item.targetTime.isAfter(from) }
        //printItems(filteredItems)
        val minutes = details
        filteredItems.forEach { item ->
            item = postponeItem(item, details.amount)
        }
    }
    private fun postpone(item: Item){
        //printItems(filteredItems)
        val newTime = item.targetTime.plusMinutes(minutes)
        println("new time ${newTime.toString()}")
        //if( item.targetTime.plusMinutes(minutes).isAfter(LocalTime.of(23, 59, 59))){
        *//* if new targetTime is before from time, it means that targetTime has been pushed
        to beginning of day, not so much postponed as preponed, which we don't want
         *//*
        if( item.targetTime.plusMinutes(minutes).isBefore(from)){
            println("don't postpone to next day")
            return LocalTime.of(23, 59, 59)
        }
        return item.targetTime.plusMinutes(minutes)
    }*/
}