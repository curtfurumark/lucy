package se.curtrune.lucy.screens.item_editor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.ItemSettingDuration
import se.curtrune.lucy.item_settings.ItemSetting

class ItemEditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = LucindaApplication.appModule.repository
            LucyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //val item = ItemsWorker.selectItem(51, applicationContext)
                    val item = repository.selectItem(10075)
                    if (item != null) {
                        ItemEditor(item =item )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemEditor(item: Item) {
    println("ItemEditor: item ${item.heading}")
    //var itemViewModel = viewModel<ItemSessionViewModel>()
    //var itemSettings = itemViewModel.getItemSettings(item, LocalContext.current)
    var heading by remember {
        mutableStateOf(item.heading)
    }
    var comment by remember {
        mutableStateOf(item.comment)
    }
    var energy by remember {
        mutableStateOf(5f)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = item.heading,
            onValueChange = {
                heading = it
            })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment,
            onValueChange = {
                comment = it
            })
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                println("on button start click")
            }) {
                Text("start")
            }
            Text(text = "00:00::00", fontSize = 24.sp)
        }
        Slider(value = energy, onValueChange = {
            println("slider on value change $it")
            energy = it
        })
        ItemSettingDuration(item = item, onEvent = {
            println("onEvent item setting duration")
        })
/*        LazyColumn{
            items(itemSettings.size){
                ItemSetting(itemSettings[it])
            }
        }*/

    }
}
@Composable
fun ItemSetting(itemSetting: ItemSetting){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 4.dp)){
       Text(text = itemSetting.heading, fontSize = 16.sp )
        var viewType = itemSetting.viewType
        if( viewType.equals(ItemSetting.ViewType.CHECKBOX)){
            Checkbox(checked = itemSetting.isChecked, onCheckedChange ={
                println(" checkbox state changed")
            } )
        }else{
            Text(text = itemSetting.value, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    LucyTheme {
    }
}