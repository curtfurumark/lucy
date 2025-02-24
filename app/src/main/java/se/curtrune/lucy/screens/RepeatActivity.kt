package se.curtrune.lucy.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Repeat
import se.curtrune.lucy.composables.RepeatDialog
import se.curtrune.lucy.persist.LocalDB
import se.curtrune.lucy.viewmodel.RepeatViewModel

class RepeatActivity : ComponentActivity() {
    //lateinit var showDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("RepeatActivity.onCreate")
        setContent {
            var showDialog by remember {
                mutableStateOf ( false )
            }
            val viewModel = viewModel<RepeatViewModel>()
            viewModel.initViewModel(LocalContext.current)
            //viewModel.showDialog
            val db: LocalDB = LocalDB(this)
            val repeats : List<Repeat> = db.selectRepeats()
            println("number of repeats ${repeats.size}");
            LucyTheme {
                // A surface container using the 'background' color from the theme
                RepeatList(list = repeats)
                if (showDialog) {
                    RepeatDialog(onDismiss = {
                        println("onDismiss")
                        showDialog = false
                    }, repeatIn = null, onConfirm = {})
                }
            }
        }
    }
}

@Composable
fun RepeatList(list: List<Repeat>){
    LazyColumn{
        itemsIndexed(list){index, item ->
            RepeatInfo(repeat = item)
        }
    }
}
@Composable
fun RepeatInfo(repeat: Repeat){
    Column(modifier = Modifier
        .padding(8.dp)
        .background(Color.Blue)
        .clickable {
            println("on repeat click id ${repeat.id}")
            /*            RepeatDialog(onDismiss = {
                println("on Dismiss")
        }, onConfirm = {
            println("onConfirm")
        }*/

        }) {
        Text(text= "id: ${repeat.id}")
        Text(text = "first date: $(repeat.firstDate.toString())" )
        Text(text = "last date ${repeat.lastDate.toString()}",color = Color.White)
        Text(text = "is infinity ${repeat.isInfinite}")
        Text(text = "updated ${repeat.updated.toString()}")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LucyTheme {
        //RepeatList(list = )
    }
}