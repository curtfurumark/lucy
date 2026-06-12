package se.curtrune.lucy.screens.appoinment

import androidx.navigation3.runtime.NavKey

sealed interface AppointmentChannel {
    data class Message(val message: String): AppointmentChannel
    data class Navigate(val navKey: NavKey): AppointmentChannel
    data object ShowAddChildDialog: AppointmentChannel
}