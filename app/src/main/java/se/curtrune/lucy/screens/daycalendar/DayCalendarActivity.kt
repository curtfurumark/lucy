package se.curtrune.lucy.screens.daycalendar

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.composables.AddItemDialog
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.activities.kotlin.composables.ItemSettings
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.daycalendar.composables.DayCalendar
import java.time.LocalTime

class DayCalendarActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucyTheme {
                // A surface container using the 'background' color from the theme
                var showAddItemDialog by remember{
                    mutableStateOf(false)
                }
                val viewModel = viewModel<DateViewModel>()
                val state = viewModel.state.collectAsState()
                Scaffold(
                    floatingActionButton = { AddItemFab {
                        println("add item fab clicked")
                        showAddItemDialog = true
                    }
                    }
                ) { it ->
                    Surface(
                        modifier = Modifier.fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        DayCalendar(state = state.value, onEvent = { event->
                            Toast.makeText(baseContext, event.toString(), Toast.LENGTH_LONG).show()
                            viewModel.onEvent(event)
                        })
                        if(showAddItemDialog){
                            AddItemDialog(
                                onDismiss = {showAddItemDialog = false},
                                onConfirm = {item ->
                                    showAddItemDialog = false
                                    viewModel.onEvent(DayEvent.AddItem(item))
                                },
                                settings = ItemSettings(targetDate = state.value.date, targetTime = LocalTime.now())
                            )
                        }
                        if( state.value.editItem){
                            println("edit item please")
                            state.value.editItem = false
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