package se.curtrune.lucy.screens.templates

import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item

sealed interface TemplateChannel {
    data class ShowUseTemplateDialog(val template: Item): TemplateChannel

    data class Navigate(val navKey: NavKey): TemplateChannel
    data class UseTemplate(val template: Item): TemplateChannel
}