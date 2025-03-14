package se.curtrune.lucy.screens.todo

import se.curtrune.lucy.classes.item.Item

sealed interface TodoEvent {
    data class Insert(val item: Item): TodoEvent
    data class Edit(val item: Item): TodoEvent
    data class Delete(val item: Item): TodoEvent
    data class Update(val item: Item): TodoEvent
    data class Postpone(val item: Item): TodoEvent
}