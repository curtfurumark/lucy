package se.curtrune.lucy.screens.templates.edit

import se.curtrune.lucy.classes.item.Item

sealed interface EditTemplateEvent {
    data class AddChild(val parent: Item): EditTemplateEvent
    data class Delete(val item: Item): EditTemplateEvent
    data class Update(val item: Item): EditTemplateEvent
}