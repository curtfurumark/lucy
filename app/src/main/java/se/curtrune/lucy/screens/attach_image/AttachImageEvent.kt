package se.curtrune.lucy.screens.attach_image

sealed interface AttachImageEvent {
    data class OnImageSuccess(val item: String): AttachImageEvent
    data object OnImageFailure: AttachImageEvent
}