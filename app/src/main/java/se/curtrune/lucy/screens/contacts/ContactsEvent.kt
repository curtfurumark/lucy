package se.curtrune.lucy.screens.contacts

sealed interface ContactsEvent {
    data class PermissionGranted(val granted: Boolean) : ContactsEvent
}