package se.curtrune.lucy.activities.kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.daycalendar.DayCalendarActivity
import se.curtrune.lucy.activities.kotlin.medicine.MedicineActivity
import se.curtrune.lucy.activities.kotlin.weekcalendar.WeekCalendarActivityKt
import se.curtrune.lucy.activities.ui.theme.LucyTheme

class IndexActivityKt : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Index()
                }
            }
        }
    }
}

@Composable
fun Index(){
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "week calendar", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, WeekCalendarActivityKt::class.java))
        })
        Text(text = "repeats", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, RepeatActivity::class.java))

        })
        Text(text = "medicin", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, MedicineActivity::class.java))

        })
        Text(text = "user settings", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, UserSettingsActivity::class.java))
        })
        Text(text = "day calendar", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, DayCalendarActivity::class.java))
        })
        Text(text = "item editor", fontSize = 24.sp, modifier = Modifier.clickable {
            context.startActivity(Intent(context, ItemEditorActivity::class.java))
        })
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    LucyTheme {
        Index()
    }
}