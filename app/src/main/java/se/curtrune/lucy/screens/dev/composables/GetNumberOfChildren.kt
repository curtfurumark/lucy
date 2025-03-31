package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.modules.LucindaApplication

@Composable
fun GetNumberOfChildren() {
    Card() {
        val localDb = LucindaApplication.appModule.sqliteLocalDB
        Column(modifier = Modifier.fillMaxWidth()) {
            var itemId by remember {
                mutableStateOf("0")
            }
            var numberOfChildren by remember {
                mutableIntStateOf(0)
            }
            OutlinedTextField(
                value = itemId,
                onValueChange = { itemId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = {
                    Text(text = "item id")
                }
            )
            Text(text = "number of children $numberOfChildren")
            Button(onClick = {
                println("on button clicked")
                numberOfChildren = localDb.getNumberChildren(itemId.toLong())
            }) {
                Text(text = "get number of children")
            }
        }
    }

}