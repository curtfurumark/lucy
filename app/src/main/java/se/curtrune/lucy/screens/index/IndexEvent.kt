package se.curtrune.lucy.screens.index

import se.curtrune.lucy.app.InitialScreen

sealed interface IndexEvent{
    data class Navigate(val firstPage: InitialScreen): IndexEvent
}