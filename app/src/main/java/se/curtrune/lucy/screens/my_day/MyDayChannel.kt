package se.curtrune.lucy.screens.my_day

sealed interface MyDayChannel{
    data class ShowMessage(val message: String): MyDayChannel
}