package se.curtrune.lucy.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.modules.LucindaApplication

@Composable
fun MentalMeter(){
    Card(modifier = Modifier.fillMaxWidth()){
        val mental = LucindaApplication.appModule.mentalModule.getCurrentMental()
        Column(){
            Text(text = "anxiety: ${mental.anxiety.toString()}", fontSize = 24.sp)
            Text(text = "energy: ${mental.energy.toString()}", fontSize = 24.sp)
            Text(text = "mood: ${mental.mood}", fontSize = 24.sp)
            Text(text = "stress: ${mental.stress}", fontSize = 24.sp)
        }

    }
}
@Preview
@Composable
fun Preview(){
    MentalMeter()
}