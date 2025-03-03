package se.curtrune.lucy.screens.dev

interface DevChannel {
    data class ShowNavigationDrawer(val show: Boolean): DevChannel
}