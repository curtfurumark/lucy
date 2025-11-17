package se.curtrune.lucy.screens.templates

import se.curtrune.lucy.classes.Template
import se.curtrune.lucy.classes.item.Item

sealed interface TemplateEvent {
    data object CreateTemplate:  TemplateEvent
    data class DeleteTemplate(val name: String):  TemplateEvent
    data class EditTemplate(val template: Item): TemplateEvent
    data class OnClick(val template: Item):  TemplateEvent
    data class OnDone(val heading: String):  TemplateEvent
    data class ShowUseTemplateDialog(val template: Item): TemplateEvent
    data class UseTemplate(val template: Item, val millis: Long): TemplateEvent

}