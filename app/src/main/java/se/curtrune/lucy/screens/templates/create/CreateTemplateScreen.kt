package se.curtrune.lucy.screens.templates.create

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.navigation.EditTemplateScreenNavKey
import se.curtrune.lucy.screens.templates.TemplateEvent
import se.curtrune.lucy.screens.templates.composables.TemplateTextField
import se.curtrune.lucy.screens.templates.edit.EditTemplateCard
import java.time.LocalTime

@Composable
fun CreateTemplateScreen(){
    var templateName by remember { mutableStateOf("") }
    val viewModel: CreateTemplateViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(70.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "skapa mall",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Icon(
                modifier = Modifier.clickable {
                    viewModel.onEvent(CreateTemplateEvent.OnSave(templateName))
                },
                imageVector = Icons.Default.Check, contentDescription = "done")
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                println("add item")
                viewModel.onEvent(CreateTemplateEvent.AddNewItem(0))
            }) {
                Text(text = "add item")
            }
            Button(onClick = {
                viewModel.onEvent(CreateTemplateEvent.OnSave(templateName))
            }) {
                Text(text = "save")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = templateName,
            onValueChange = {
                templateName = it
                viewModel.onEvent(CreateTemplateEvent.OnTemplateNameChanged(it))
            },
            label = { Text(text = "mall namn") },
        )
        EditTemplateCard(item = state.template, onEvent = {
            viewModel.onEvent(it)
        })
        state.items.forEachIndexed { index, item -> //TemplateTextField(item, onEvent = {
            EditTemplateCard(item = item, onEvent = {})
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.channel.collect { channel ->
            when (channel) {
                is CreateTemplateChannel.ShowMessage -> {
                    println(channel.message)
                    Toast.makeText(context, channel.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}



@Composable
@Preview
fun PreviewCreateTemplateTab(){
    //CreateTemplateTab()
    val item = Item("test").also {
        it.targetTime = LocalTime.of(12, 0)
    }
    TemplateTextField(item = item, onEvent = {})
}
