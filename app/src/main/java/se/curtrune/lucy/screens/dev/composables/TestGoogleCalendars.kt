package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.LucindaApplication

@Composable
fun TestGoogleCalendars(){
    Column(modifier = Modifier.fillMaxWidth()){
        val calendarModule = LucindaApplication.calendarModule
        Text(text = "just testing calendar module")

    }
}