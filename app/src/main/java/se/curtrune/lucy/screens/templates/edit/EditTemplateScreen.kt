package se.curtrune.lucy.screens.templates.edit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditTemplateScreen(templateID: Long,onDone: () -> Unit){
    val context = LocalContext.current
    val viewModel: EditTemplateViewModel = viewModel(){
        EditTemplateViewModel.factory(templateID).create(EditTemplateViewModel::class.java)
    }

    val state by viewModel.state.collectAsState()
    val template by remember { mutableStateOf(state.template) }

    var heading by remember { mutableStateOf(state.template.heading) }

    Column(modifier = Modifier.fillMaxWidth()){
        Spacer(Modifier.height(70.dp))
        Text(text = "edit template")
        Button(onClick = {
            viewModel.onEvent(EditTemplateEvent.AddChild(template))

        }){
            Text(text = "Add Item")
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = heading,
                onValueChange = {
                    heading = it
                    template.heading = it
                    viewModel.onEvent(EditTemplateEvent.Update(template))
                })
            Icon(
                modifier = Modifier.clickable {
                    onDone()
                },
                imageVector = Icons.Default.Check,
                contentDescription = "done")
        }
        state.children.forEach{
            println("...child ${it.heading} ID ${it.id}")
            EditTemplateCard(it, { event ->
                viewModel.onEvent(event)
            })
            Spacer(Modifier.height(4.dp))
        }
    }
    LaunchedEffect(Unit){
        viewModel.channel.collect {
            when(it){
                is EditTemplateChannel.ShowMessage ->{
                    println(it.message)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                EditTemplateChannel.NavigateBack -> {
                    onDone()
                }

                is EditTemplateChannel.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}
