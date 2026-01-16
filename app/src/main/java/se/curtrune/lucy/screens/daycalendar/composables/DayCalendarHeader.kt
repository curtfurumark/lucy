package se.curtrune.lucy.screens.daycalendar.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.main.LucindaFragment

@Composable
fun DayCalenderHeader(header: String, modifier: Modifier = Modifier, onClick: (Boolean)->Unit ){
    var showItems by remember{
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier.fillMaxWidth()
            .padding(bottom = 4.dp)
            .clickable(onClick = {
                showItems= !showItems
                onClick(showItems)
            })){
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            text = header)

    }
}