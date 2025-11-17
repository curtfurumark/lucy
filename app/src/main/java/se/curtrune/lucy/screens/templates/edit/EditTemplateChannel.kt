package se.curtrune.lucy.screens.templates.edit

sealed interface EditTemplateChannel {
    data class Error(val message: String) : EditTemplateChannel
    data class ShowMessage(val message: String) : EditTemplateChannel
    data object NavigateBack: EditTemplateChannel
}