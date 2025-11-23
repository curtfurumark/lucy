package se.curtrune.lucy.screens.templates.templates

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.templates.TemplateChannel
import se.curtrune.lucy.screens.templates.TemplateEvent
import se.curtrune.lucy.screens.templates.composables.TemplateCard

@Composable
fun TemplatesScreen(navigate: (NavKey)->Unit){
    val viewModel: TemplatesViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var useTemplateDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.onEvent(TemplateEvent.CreateTemplate)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add template")
        }

    }) {paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "dina mallar",
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleLarge.fontSize)
            Spacer(modifier = Modifier.height(16.dp))
            state.templates.forEach {template ->
                Spacer(modifier = Modifier.height(4.dp) )
                TemplateCard(template = template) { event ->
                    viewModel.onEvent(event)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is TemplateChannel.UseTemplate -> {
                    //useTemplateDialog = true
                    Toast.makeText(context, "use template ${event.template.heading} deprecated.", Toast.LENGTH_SHORT).show()
                }


                is TemplateChannel.Navigate -> {
                    navigate(event.navKey)
                    //Toast.makeText(context, "navigate to ${event.route}.", Toast.LENGTH_SHORT).show()
                }

                is TemplateChannel.ShowUseTemplateDialog -> {
                    useTemplateDialog= true
                    //Toast.makeText(context, "show use template dialog.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    if( useTemplateDialog){
        val template = state.selectedTemplate
        if( template == null){
            Toast.makeText(context, "no template selected.", Toast.LENGTH_SHORT).show()
            return
        }
        UseTemplateDialog(
            template = template,
            onDismiss = {
                useTemplateDialog = false},
            onConfirm = {
                viewModel.onEvent(TemplateEvent.UseTemplate(template, millis = it))
                useTemplateDialog = false
            })
    }
}

@Composable
fun UseTemplateDialog(
    template: Item,
    onDismiss: ()-> Unit,
    onConfirm: (Long)-> Unit){
    var name by remember { mutableStateOf("") }
    Dialog(onDismissRequest = {onDismiss()}){
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        Column {
            Text(text = "use template ${template.heading}")
            //OutlinedTextField(value = name, onValueChange = {name = it}, label = { Text(text = "name")})
            DatePicker(state =datePickerState)
            Row(modifier = Modifier.fillMaxWidth()){
                Button(onClick = onDismiss) {
                    Text(text = "cancel")
                }
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { onConfirm(it) }
                }) {
                    Text(text = "confirm")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTemplatesTab(){
    TemplatesScreen(navigate = {})
    UseTemplateDialog(template = Item("heading"), onDismiss = {}) { }

}