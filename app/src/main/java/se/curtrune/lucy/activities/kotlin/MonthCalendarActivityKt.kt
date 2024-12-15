package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.LucindaTopAppBar
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.viewmodel.CalendarMonthViewModel
import java.time.YearMonth

class MonthCalendarActivityKt : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = { LucindaTopAppBar()}) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val monthViewModel = viewModel(CalendarMonthViewModel::class.java)
                        monthViewModel.setYearMonth(YearMonth.now(), LocalContext.current)
                        val calendarDates = monthViewModel.calendarDates
                        calendarDates.value?.let { MonthCalendar(it) }
                    }
                }
            }
        }
    }
}
@Composable
fun AddItemFloatingActionButton(){
    FloatingActionButton(onClick = { /*TODO*/ }) {
/*        Icon(imageVector = Icons.Default.Add, modifier =  Modifier, color = Color.Green){

        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    LucyTheme {
        //MonthCalendar()
    }
}