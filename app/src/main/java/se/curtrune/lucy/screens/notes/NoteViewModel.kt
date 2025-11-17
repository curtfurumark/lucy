package se.curtrune.lucy.screens.notes

import androidx.lifecycle.ViewModel
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.item.Item

class NoteViewModel: ViewModel() {
    val repo = LucindaApplication.appModule.repository
    init {
        println("NoteViewModel created")

    }
    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.OnSave -> {
                onSave(event.title, event.content)
            }
        }
    }
    private fun onSave(title: String, content: String){
        println("...saving note $title")
        val item = Item().also {
            it.heading = title
            it.comment = content
        }
        repo.insert(item)
        println("...note saved")
    }

}