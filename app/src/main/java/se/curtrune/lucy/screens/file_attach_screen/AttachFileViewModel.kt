package se.curtrune.lucy.screens.file_attach_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item

class AttachFileViewModel(private val item: Item): ViewModel() {

    private val db = LucindaApplication.appModule.repository
    private val _state = MutableStateFlow(AttachFileState(item))
    val state = _state.asStateFlow()
    init {
        println("AttachFileViewModel(item)")

    }
    fun onEvent(event: AttachFileEvent){
        when(event) {
            is AttachFileEvent.OnHeadingChanged -> onHeadingChanged(event.heading)
        }
    }

    fun onFileSelected(uri: Uri) {
        println("...onFileSelected($uri)")
        val child = Item(_state.value.heading).also {
            //it.description = it.toString()
            //it.comment = uri.path.toString()
            it.comment= uri.toString()
            it.type = Type.MEDIA.ordinal
            it.heading = state.value.heading
        }
        val res = db.insertChild(item, child)
        println("res: $res")
    }
    private fun onHeadingChanged(heading: String){
        println("...onHeadingChanged($heading)")
        _state.update { it.copy(
            heading = heading
            )
        }
    }

    init {
        println("AttachFileViewModel(item)")
    }

}