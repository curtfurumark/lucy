package se.curtrune.lucy.activities.kotlin.medicine

import se.curtrune.lucy.classes.Item

data class MedicineState(
    val items: List<Item> = emptyList<Item>(),
    val message: String = "",
    val errorMessage: String = "",
    val showAddMedicineDialog: Boolean = false
)