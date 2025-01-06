package se.curtrune.lucy.activities.kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.composables.AddItemDialog
import se.curtrune.lucy.activities.kotlin.composables.AddItemFab
import se.curtrune.lucy.activities.kotlin.daycalendar.DateEvent
import se.curtrune.lucy.activities.kotlin.daycalendar.DayCalendar
import se.curtrune.lucy.activities.kotlin.composables.LucindaTopAppBar
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.activities.kotlin.viewmodels.DateViewModel
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.workers.ItemsWorker
import java.time.LocalDate

class DayCalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = ItemsWorker.selectItems(LocalDate.now(), applicationContext)

        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                var showAddItemDialog by remember{
                    mutableStateOf(false)
                }
                Scaffold(
                    topBar = {LucindaTopAppBar(Mental(), onEvent = {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                    })},
                    floatingActionButton = { AddItemFab {
                        println("add item fab clicked")
                        showAddItemDialog = true
                    }}
                ) { it ->
                    Surface(
                        modifier = Modifier.fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        var dateViewModel = viewModel<DateViewModel>()

                        DayCalendar(items = items, onEvent = { event->
                            Toast.makeText(baseContext, event.toString(), Toast.LENGTH_LONG).show()
                            dateViewModel.onEvent(event)
                        })
                        if(showAddItemDialog){
                            AddItemDialog(onDismiss = {showAddItemDialog = false},
                                onConfirm = {item ->
                                    showAddItemDialog = false
                                    dateViewModel.onEvent(DateEvent.AddItem(item))
                                }
                            )
                        }
                    }
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