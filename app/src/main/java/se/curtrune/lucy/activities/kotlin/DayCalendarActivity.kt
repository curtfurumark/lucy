package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.curtrune.lucy.activities.kotlin.composables.DayCalendar
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.workers.ItemsWorker
import java.time.LocalDate

class DayCalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = ItemsWorker.selectItems(LocalDate.now(), applicationContext)

        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DayCalendar(items = items)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    LucyTheme {
    }
}