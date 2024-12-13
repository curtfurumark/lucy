package se.curtrune.lucy.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.classes.Repeat
import java.util.ArrayList

class RepeatViewModel: ViewModel() {
    //var mutableRepeats: MutableList<List<Repeat>> = MutableList()
/*    fun getRepeats(): List<Repeat>{
        return ArrayList()<Repeat>()
    }*/
    var showDialog =  mutableStateOf(false)
    fun onRepeatClick(repeat: Repeat){
        showDialog.value = true
    }
    fun initViewModel(context: Context){
        println("...initViewModel(Context)")
    }

}