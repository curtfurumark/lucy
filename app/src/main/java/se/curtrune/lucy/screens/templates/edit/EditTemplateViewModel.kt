package se.curtrune.lucy.screens.templates.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item

class EditTemplateViewModel(val templateID: Long): ViewModel() {
    private val db = LucindaApplication.Companion.appModule.repository
    private val _state = MutableStateFlow(EditTemplateState())
    val state = _state.asStateFlow()
    private val _channel = Channel<EditTemplateChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        println("EditTemplateViewModel.init")
        val template = db.selectItem(templateID)
        if( template == null) {
            error("no template with id $templateID")
        }else {
            val children = db.selectChildren(template)
            _state.update {
                it.copy(
                    template = template,
                    children = children
                )
            }
        }
    }

    fun onEvent(event: EditTemplateEvent){
        when(event){
            is EditTemplateEvent.Update->{
                update(event.item)
            }
            is EditTemplateEvent.AddChild -> {
                addChild(event.parent)
            }
            is EditTemplateEvent.Delete -> {
                delete(event.item)
            }
        }
    }
    private fun selectTree(templateID: Long): Item?{
        println("...selecting tree for id $templateID")
        val template = db.selectItem(templateID)
        if( template == null) {
            //error("no template with id $templateID")
            return null
        }
        println("...selected template")
        val children = db.selectChildren(template)
        println("...number of children ${children.size}")
        template.children = children as MutableList<Item>?
        return template
    }
    private fun addChild(parent: Item){
        println("EditTemplateViewMode.addChild ${parent.heading}")
        val child = Item().also {
            it.parent = parent
        }
        db.insertChild(parent = parent, child = child)
        val template =db.selectItem(templateID)
        if( template == null) {
            error("no template with id $templateID")
            return
        }
        val children = db.selectChildren(template)
        //template.children = children as MutableList<Item>?
        //val children = template.children.sortedBy { it.targetTime }
        children.sortedWith { o1, o2 -> o1.targetTime.compareTo(o2.targetTime) }
        println("...children sorted")
        children.forEach {
            println("...child ${it.heading} id: ${it.id}")
        }
        _state.update { it.copy(
            template = template,
            children = children
        ) }
    }
    private fun delete(item: Item){
        println("deleting template ${item.heading}")
        val stat = db.delete(item)
        if( !stat){
            error("failed to delete template ${item.heading}")
            return
        }
        val template = db.selectTree(templateID)
        if( template == null) {
            error("no template with id $templateID")
            return
        }
        println("...updated template")
        printTree(template)
        _state.update { it.copy(
                template = template
            )
        }
    }
    private fun error(message: String){
        println("ERROR: $message")
        viewModelScope.launch {
            _channel.send(EditTemplateChannel.Error(message))
        }
    }
    private fun printTree(item: Item){
        println("printTree heading: ${item.heading}")
        println("...number of children ${item.children?.size}")
        item.children?.forEach {
            println("...child heading: ${it.heading}, id: ${it.id}")
        }
    }

    private fun update(item: Item){
        println("updating template")
        val rowsAffected = db.update(item)
        if( rowsAffected != 1){
            error("failed to update template ${item.heading}")
        }
    }

    companion object {
        fun factory(id: Long): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EditTemplateViewModel(id) as T
                }
            }
        }
    }
}