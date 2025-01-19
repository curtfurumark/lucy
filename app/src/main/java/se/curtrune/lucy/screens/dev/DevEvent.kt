package se.curtrune.lucy.screens.dev

sealed interface DevEvent{
    data object CreateItemTree: DevEvent
}