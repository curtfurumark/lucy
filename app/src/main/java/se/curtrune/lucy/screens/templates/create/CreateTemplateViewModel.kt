package se.curtrune.lucy.screens.templates.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.templates.edit.EditTemplateEvent
import kotlin.collections.plus

class CreateTemplateViewModel: ViewModel() {
    private val _state = MutableStateFlow(CreateTemplateState())
    val state = _state.asStateFlow()
    private val db = LucindaApplication.Companion.appModule.repository
    init {
        println("CreateTemplateViewModel.init")
        val template = Item().also {
            it.type = Type.TEMPLATE
        }
        _state.update { it.copy(
            template = template,
        ) }
    }
    fun onEvent(event: EditTemplateEvent){
        when(event){
            is EditTemplateEvent.AddChild ->{}
            is EditTemplateEvent.Delete -> {}
            is EditTemplateEvent.Update -> {}
        }

    }
    fun onEvent(event: CreateTemplateEvent) {
        when (event) {
            is CreateTemplateEvent.AddNewItem -> {
                createItem(event.index)
            }
            is CreateTemplateEvent.OnSave -> {
                onDone(event.heading)
            }
            is CreateTemplateEvent.OnUpdate -> {
                updateItem(event.item)

            }


            is CreateTemplateEvent.OnTemplateNameChanged -> {
                _state.value.template.heading = event.name
/*                _state.update { it.copy(
                    template = _state.value.template,
                ) }*/
            }
        }
    }

    private fun createItem(index: Int){
        _state.update { it.copy(
            items = it.items + Item()
        ) }
    }
    private fun createTemplate(name: String){
        val item = Item().also {
            it.heading = name
            it.type = Type.TEMPLATE
        }
    }
    private fun deleteTemplate(){
        println("deleteTemplate")
    }
    private fun onDone(heading:String){
        println("...onDone($heading)")
        val template = Item().also {
            it.heading = heading
            it.type = Type.TEMPLATE
        }
        val  tmp = db.insert(template)
        if( tmp == null){
            println("ERROR FFS, could not insert template")
            return
        }
        println("template id: $tmp.id")
        println("root template: ${template.heading}")
        _state.value.items.forEach {
            it.parentId = tmp.id
            db.insert(it)
            println("template item: $it.heading")
        }
    }
    private fun updateItem(item: Item){
        println("...updateItem(${item.heading})")
        //db.update(item)
    }
    private fun updateTemplate(){
        println("updateTemplate")
    }
}