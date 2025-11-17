package se.curtrune.lucy.screens.notes

sealed interface NoteEvent {
    data class OnSave(val title: String, val content: String): NoteEvent

}