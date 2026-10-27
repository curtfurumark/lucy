package se.curtrune.lucy.screens.settings

import androidx.navigation3.runtime.NavKey

sealed interface UserChannel{
    data class Navigate(val navKey: NavKey): UserChannel
    data object ReadWriteCalendarPermissions: UserChannel
    data class ShowMessage(val message: String):UserChannel
}
