package se.curtrune.lucy.screens.appoinment

import se.curtrune.lucy.classes.item.Item

sealed interface AppointmentEvent {
    data object AttachFile: AppointmentEvent
    data object AddCheckableNote: AppointmentEvent
    data class AddChild(val item: Item): AppointmentEvent
    data object AddContact: AppointmentEvent
    data object AddVoiceRecording: AppointmentEvent
    data object AddSummary: AppointmentEvent
    data object AddDescription: AppointmentEvent
    data object AddToTimeLine: AppointmentEvent
    data object Pending: AppointmentEvent
    data class Update(val item: Item): AppointmentEvent

}