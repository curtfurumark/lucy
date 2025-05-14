package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.dev.ui.theme.LucyTheme
import se.curtrune.lucy.screens.dev.DevEvent
import se.curtrune.lucy.screens.dev.DevState

@Composable
fun HolidaysTest(state: DevState, onEvent: (DevEvent)->Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text(
                text = "test of get holidays",
                fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                onEvent(DevEvent.GetHolidays)
            }){
                Text(text = "get holidays")
            }
        }

    }
}

@Composable
@PreviewLightDark
fun PreviewHolidays(){
    LucyTheme {
        HolidaysTest(state = DevState(), onEvent = {})
    }
}