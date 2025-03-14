package se.curtrune.lucy.screens.medicine

import se.curtrune.lucy.classes.item.Item

sealed interface MedicineChannelEvent {
    data class ShowMessage(val message: String): MedicineChannelEvent
    data class ShowProgressBar(val show: Boolean): MedicineChannelEvent
    data object ShowAdverseEffectsDialog: MedicineChannelEvent
    data class Edit(val item: Item): MedicineChannelEvent
}