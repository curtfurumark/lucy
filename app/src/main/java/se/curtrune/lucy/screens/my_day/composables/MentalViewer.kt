package se.curtrune.lucy.screens.my_day.composables

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Mental

@Composable
fun MentalViewer(mental: Mental){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "energy ${mental.energy}",
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "mood: ${mental.mood}",
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "stress: ${mental.stress}",
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "anxiety: ${mental.anxiety}",
            color = MaterialTheme.colorScheme.onBackground)
    }
}


@Composable
@Preview(showBackground = true)
@PreviewLightDark
fun PreviewMentalViewer(){
    LucyTheme {
        val mental = Mental()
        MentalViewer(mental = mental)
    }
}