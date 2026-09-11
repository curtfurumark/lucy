package se.curtrune.lucy.screens.edit

import se.curtrune.lucy.classes.item.Item

data class ItemEditorState(
    val item: Item =            Item(),
    val seconds: Long = 0,
    val timerState: ItemEditorViewModel.TimerState = ItemEditorViewModel.TimerState.PENDING,
    val categories: List<String> = emptyList(),
)
