package se.curtrune.lucy.screens.main

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.affirmations.Affirmation
import se.curtrune.lucy.screens.affirmations.Quote

sealed interface MainChannelEvent{
    data object  UpdateAvailable: MainChannelEvent
    data class ShowBoostDialog(val quote: Quote? = null): MainChannelEvent
    data class ShowAffirmation(val affirmation: Affirmation? = null): MainChannelEvent
    data object ShowPanicDialog: MainChannelEvent
    data object OpenNavigationDrawer : MainChannelEvent
    data object ShowDayCalendar : MainChannelEvent
    data class StartSequence(val root: Item) : MainChannelEvent
    data class ShowQuoteDialog(val quote: Quote? = null): MainChannelEvent
}