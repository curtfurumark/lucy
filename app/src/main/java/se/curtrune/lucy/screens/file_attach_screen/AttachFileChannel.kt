package se.curtrune.lucy.screens.file_attach_screen

import androidx.navigation3.runtime.NavKey

sealed interface AttachFileChannel {
    data class Navigate(val key: NavKey): AttachFileChannel
    data object OnBack: AttachFileChannel
}