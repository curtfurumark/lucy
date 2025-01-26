package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

class TabStack(val date: LocalDate) {
    private val stack = mutableListOf<Item>()
    val items = stack
    fun pushItem(item: Item){
        stack.add(item)

    }
    fun pop(): Item?{
        if(stack.size < 1){
            return null
        }
        return stack.removeAt(stack.size -1)
    }
    fun select(index: Int){

    }
}