package se.curtrune.lucy.activities.kotlin.viewmodels

import androidx.lifecycle.ViewModel
import se.curtrune.lucy.activities.kotlin.daycalendar.DateEvent

class DateViewModel: ViewModel(){

    fun onEvent(event: DateEvent){
        println("DateViewModel.onEvent(DateEvent) ${event.toString()}")
        when(event){
            is DateEvent.AddItem -> {println("add item")}
            is DateEvent.CurrentDate -> {println("current date change")}
            is DateEvent.DeleteItem -> {println("delete item")}
            is DateEvent.EditItem -> { println("edit item") }
            is DateEvent.ShowActionsMenu -> {println("show action menu")}
            is DateEvent.UpdateItem -> println("update item")
            is DateEvent.EditTime -> {println("edit time")}
            else ->{ println("or else !!")
            }
        }
    }
}