package se.curtrune.lucy.screens.main

import se.curtrune.lucy.screens.affirmations.Affirmation
import se.curtrune.lucy.screens.affirmations.Quote

sealed interface MainChannelEvent{
    data object  UpdateAvailable: MainChannelEvent
    data class ShowBoostDialog(val quote: Quote? = null): MainChannelEvent
    data class ShowAffirmation(val affirmation: Affirmation? = null): MainChannelEvent
    data object ShowPanicDialog: MainChannelEvent
    data class ShowQuoteDialog(val quote: Quote? = null): MainChannelEvent
}