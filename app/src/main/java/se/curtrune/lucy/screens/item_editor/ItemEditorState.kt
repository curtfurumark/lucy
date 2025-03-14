package se.curtrune.lucy.screens.item_editor

import se.curtrune.lucy.classes.item.Item

data class ItemEditorState(
    val item: Item? = null,
    val seconds: Long = 0,
    val timerState: ItemSessionViewModel.TimerState = ItemSessionViewModel.TimerState.PENDING
)
