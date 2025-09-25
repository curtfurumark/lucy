package se.curtrune.lucy.screens.settings

sealed interface UserChannel{
    data object ReadWriteCalendarPermissions: UserChannel
    data class ShowMessage(val message: String):UserChannel
}
