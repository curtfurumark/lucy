package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.curtrune.lucy.composables.ScrollableMonthPicker
import java.time.YearMonth

@Composable
fun TestScrollableYearMonth(){
    Card(modifier = Modifier.fillMaxWidth()){
        var currentYearMonth by remember {
            mutableStateOf(YearMonth.now())
        }
        Column() {
            Text(text = "test scrollable yearMonth")
            Text(text = currentYearMonth.toString())
            ScrollableMonthPicker { yearMonth ->
                currentYearMonth = yearMonth
            }
        }
    }
}