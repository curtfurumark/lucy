package se.curtrune.lucy.screens.dev.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestGoogleCalendars(){
    var calendarID by remember {
        mutableStateOf("7")
    }
    val calendarModule = LucindaApplication.calendarModule
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = "just testing calendar module")
        OutlinedTextField(value = calendarID, onValueChange = {
            calendarID = it
        })
        Button(onClick = {
            val items = calendarModule.getGoogleEventsAsItems(calendarID.toInt())
            items.forEach{ item ->
                println(item.toString())
            }
        }){
            Text(text = "get items")
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun PreviewTestGoogleCalendars(){
    LucyTheme {
        TestGoogleCalendars()
    }
}