package se.curtrune.lucy.screens.templates.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.firebase.dataconnect.LocalDate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.navigation.CreateTemplateScreenNavKey
import se.curtrune.lucy.screens.navigation.EditTemplateScreenNavKey
import se.curtrune.lucy.screens.templates.TemplateChannel
import se.curtrune.lucy.screens.templates.TemplateEvent
import se.curtrune.lucy.screens.templates.templates.TemplatesState
import java.time.Instant
import java.time.ZoneId

class TemplatesViewModel: ViewModel() {
    private val _state = MutableStateFlow(TemplatesState())
    val state = _state.asStateFlow()
    private val _channel = Channel<TemplateChannel>()
    val channel = _channel.receiveAsFlow()
    private val db = LucindaApplication.Companion.appModule.repository
    init {
        println("TemplatesViewModel.init")
        val templates = selectTemplates()
        _state.update { it.copy(
            templates = templates
        ) }
    }
    fun onEvent(event: TemplateEvent){
        println("TemplatesViewModel.onEvent $event")
        when(event){
            is TemplateEvent.CreateTemplate -> {
                createTemplate()
            }
            is TemplateEvent.DeleteTemplate -> {
                deleteTemplate(event.template)
            }
            is TemplateEvent.EditTemplate ->{
                println("editTemplate")
                editTemplate(event.template)
            }
            is TemplateEvent.OnClick -> {
                println("clicked item ${event.template.heading}")
                onClickTemplate(event.template)
            }

            is TemplateEvent.OnDone -> {
                println("onDone templateViewModel")
            }
            is TemplateEvent.ShowUseTemplateDialog -> {
                showUseTemplateDialog(event.template)
            }

            is TemplateEvent.UseTemplate -> {
                useTemplate(event.template, event.millis)
            }
        }
    }
    private fun copy(template: Item): Item{
        val item = Item()
        item.heading = template.heading
        item.description = template.description
        item.targetTime = template.targetTime
        return item
    }
    private fun createTemplate(){
        println("TemplatesViewModel.createTemplate()")
        viewModelScope.launch {
            _channel.send(TemplateChannel.Navigate(CreateTemplateScreenNavKey))
        }

    }
    private fun deleteTemplate(template: Item){
        println("deleteTemplate ${template.heading}")
        val stat = db.delete(template)
        if( template.hasChild()){
            template.children.forEach {
                db.delete(it)
            }
        }
        _state.update { it.copy(
            templates = selectTemplates()
        ) }
    }
    private fun editTemplate(template: Item){
        viewModelScope.launch {
            _channel.send(TemplateChannel.Navigate(EditTemplateScreenNavKey(template.id)))
        }
    }
    private fun onClickTemplate(template: Item){
        println("onClickTemplate")
        val items = db.selectChildren(template)
        println("items: ${items.size}")
        template.setChildren(items)
    }
    private fun selectTemplates(): List<Item>{
        println("selectTemplates")
        val templates = db.selectItems(Type.TEMPLATE)
        templates.forEach {
            //println("template: ${it.heading}")'
            it.children = db.selectChildren(it)
        }
        return templates

    }

    private fun showUseTemplateDialog(template: Item){
        println("showUseTemplateDialog")
        viewModelScope.launch {
            _state.update { it.copy(
                selectedTemplate = template
            ) }
            _channel.send(TemplateChannel.ShowUseTemplateDialog(template))
        }
    }

    private fun useTemplate(template: Item,epochMillis: Long){
        println("useTemplate ${template.heading} $epochMillis")
        val actual = copy(template = template)
        val date = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        actual.targetDate = date
        if( template.hasChild()){
            template.children.forEach {
                val item = copy(template = it)
                item.targetDate = date
                println("actual item ${item}")
                db.insert(item)
            }
        }
    }
}