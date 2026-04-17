package se.curtrune.lucy.screens.my_manual

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.my_manual.form.DataOne

sealed interface MyManualGuideEvent{
    data class OnNameChanged(val name: String): MyManualGuideEvent
    data class OnPageOneCompleted(val data: DataOne): MyManualGuideEvent
    data object OnPageTwoCompleted: MyManualGuideEvent
    data object OnPageThreeCompleted: MyManualGuideEvent
    data object OnPageFourCompleted: MyManualGuideEvent
    data object OnBackPressed: MyManualGuideEvent

    data class OnAddMedicine(val medicine: Item): MyManualGuideEvent
    data class OnAddSensitivity(val sensitivity: Item): MyManualGuideEvent
    data class OnAddTrigger(val trigger: Item): MyManualGuideEvent

}