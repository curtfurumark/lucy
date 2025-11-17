package se.curtrune.lucy.screens.templates.create

import se.curtrune.lucy.classes.item.Item

sealed interface CreateTemplateEvent {
    data class AddNewItem(val index: Int): CreateTemplateEvent
    data class OnSave(val heading: String) : CreateTemplateEvent
    data class OnTemplateNameChanged(val name: String) : CreateTemplateEvent
    data class OnUpdate(val item: Item): CreateTemplateEvent

}