package se.curtrune.lucy.composables.top_app_bar

interface TopAppBarEvent {
    data class OnSearch(val filter: String, val everywhere: Boolean): TopAppBarEvent
    data object OnPanic: TopAppBarEvent
    data object OnBoost: TopAppBarEvent
}