package se.curtrune.lucy.screens.medicine

import se.curtrune.lucy.classes.item.Item

data class MedicineState(
    val items: List<Item> = emptyList<Item>(),
    val message: String = "",
    val errorMessage: String = "",
    val showAddMedicineDialog: Boolean = false
)