package se.curtrune.lucy.screens.file_attach_screen

sealed interface AttachFileEvent {
    data class OnHeadingChanged(val heading: String): AttachFileEvent
}