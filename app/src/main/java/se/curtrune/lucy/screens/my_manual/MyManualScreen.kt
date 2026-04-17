package se.curtrune.lucy.screens.my_manual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyManualScreen(modifier: Modifier = Modifier){
    val viewModel: MyManualGuideViewmodel    = viewModel<MyManualGuideViewmodel>()
    val state by viewModel.state.collectAsState()
    Column(
        modifier = modifier.fillMaxSize(),
    ){
        Text(text = "my manual", style = MaterialTheme.typography.headlineLarge)
        MyManualGuide(state = state, onEvent = viewModel::onEvent)
    }
}