package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.classes.Item


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