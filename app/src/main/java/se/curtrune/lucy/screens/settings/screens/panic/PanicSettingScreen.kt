package se.curtrune.lucy.screens.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.screens.settings.screens.panic.BreatheSettingCard
import se.curtrune.lucy.screens.settings.screens.panic.PanicSettingEvent
import se.curtrune.lucy.screens.settings.screens.panic.PanicSettingViewModel


@Composable
fun PanicSettingScreen(modifier: Modifier = Modifier){
    val viewModel = viewModel() {
        PanicSettingViewModel()
    }
    Column(modifier = modifier.fillMaxSize()){
        Text(text = "ice")
        Text(text = "eat a lemon")
        Text(text = "take a walk")
        Text(text = "cold shower")
        BreatheSettingCard(onBreatheChecked = {
            viewModel.onEvent(PanicSettingEvent.SetBreathe(it))
        })
    }

}

@Composable
@PreviewLightDark
fun PreviewPanicSettingScreen(){
    PanicSettingScreen()

}