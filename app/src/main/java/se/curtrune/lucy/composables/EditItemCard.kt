package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp


@Composable
fun DurationEdit(duration: Long, onDurationEdit: (Long)->Unit){
    var showDurationDialog by remember{
        mutableStateOf(false)
    }
    var seconds by remember{
        mutableLongStateOf(duration)
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable { showDurationDialog = true }){
        TimeText(seconds, 20.sp)
    }
    if(showDurationDialog){
        DurationDialog(initialDuration = seconds, onDismiss ={
            showDurationDialog = false
        } , onConfirm = {
            println("seconds $it")
            seconds = it
            onDurationEdit(seconds)
            showDurationDialog = false
        })
    }
}