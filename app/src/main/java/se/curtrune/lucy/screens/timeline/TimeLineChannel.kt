package se.curtrune.lucy.screens.timeline

import androidx.navigation3.runtime.NavKey

sealed interface TimeLineChannel {
    data class Navigate(val navKey: NavKey): TimeLineChannel
}
