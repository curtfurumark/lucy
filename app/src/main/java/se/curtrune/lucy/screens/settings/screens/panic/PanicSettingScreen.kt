package se.curtrune.lucy.screens.settings.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.classes.PanicOption
import se.curtrune.lucy.screens.settings.screens.panic.BreatheSettingCard
import se.curtrune.lucy.screens.settings.screens.panic.Five4321Card
import se.curtrune.lucy.screens.settings.screens.panic.ICECard
import se.curtrune.lucy.screens.settings.screens.panic.PanicOptionCard
import se.curtrune.lucy.screens.settings.screens.panic.PanicSettingChannel
import se.curtrune.lucy.screens.settings.screens.panic.PanicSettingEvent
import se.curtrune.lucy.screens.settings.screens.panic.PanicSettingState
import se.curtrune.lucy.screens.settings.screens.panic.PanicSettingViewModel
import se.curtrune.lucy.screens.settings.screens.panic.UrlCard


@Composable
fun PanicSettingScreen(modifier: Modifier = Modifier){
    val viewModel = viewModel() {
        PanicSettingViewModel()
    }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()){
        Text(text = "ice")
        Text(text = "eat a lemon")
        Text(text = "take a walk")
        Text(text = "cold shower")
        BreatheSettingCard(state = PanicSettingState(), onBreatheChecked = {
            viewModel.onEvent(PanicSettingEvent.SetBreathe(it))
        })
        Spacer(modifier = Modifier.height(1.dp))
        ICECard(state = state, onEvent = {
            viewModel.onEvent(it)
        })
        Spacer(modifier = Modifier.height(1.dp))
        PanicOptionCard(option =state.panicOption)
        Spacer(modifier = Modifier.height(1.dp))
        UrlCard(
            state = state,
            onChecked = {
                viewModel.onEvent(PanicSettingEvent.SetUrlOption(it))
            },
            onUrlChange = {
                viewModel.onEvent(PanicSettingEvent.SetUrl(it))
            }
        )
        Spacer(modifier = Modifier.height(1.dp))
        Five4321Card(state = state, onChecked = {
            viewModel.onEvent(PanicSettingEvent.SetPanicOption(PanicOption.FIVE4321))
        })
    }
    LaunchedEffect(Unit){
        viewModel.channel.collect{
            when(it){
                is PanicSettingChannel.Message -> {
                    println(it.message)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

@Composable
@PreviewLightDark
fun PreviewPanicSettingScreen(){
    PanicSettingScreen()

}