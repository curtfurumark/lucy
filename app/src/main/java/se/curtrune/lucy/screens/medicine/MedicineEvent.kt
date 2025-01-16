package se.curtrune.lucy.screens.medicine

import se.curtrune.lucy.classes.Item

sealed interface MedicineEvent {
    data class Delete(val item: Item): MedicineEvent
    data class Edit(val item: Item): MedicineEvent
    data class Insert(val item: Item): MedicineEvent
    data class ShowAddMedicineDialog(val show: Boolean): MedicineEvent
    data class Sort(val sortType: SortType): MedicineEvent
}
enum class SortType{
    NAME, ADDED, UPDATED
}