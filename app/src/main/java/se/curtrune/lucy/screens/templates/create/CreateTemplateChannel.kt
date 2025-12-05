package se.curtrune.lucy.screens.templates.create

interface CreateTemplateChannel {
    data class ShowMessage(val message: String) : CreateTemplateChannel
}