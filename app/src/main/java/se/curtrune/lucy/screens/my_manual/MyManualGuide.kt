package se.curtrune.lucy.screens.my_manual

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.screens.my_manual.form.PageOne
import se.curtrune.lucy.screens.my_manual.form.PageSensories
import se.curtrune.lucy.screens.my_manual.form.PageThree
import se.curtrune.lucy.screens.my_manual.form.PageTwo

@Composable
fun MyManualGuide(state: MyManualGuideState, onEvent: (MyManualGuideEvent)->Unit){
    val viewModel= viewModel<MyManualGuideViewmodel>()
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "page: ${state.currentPage + 1}/4")
        when(state.currentPage){
            0 -> PageOne(state = state, onEvent = onEvent )
            1 -> PageTwo( state = state, onEvent = onEvent)
            2 -> PageThree(state = state, onEvent = onEvent)
            3 -> PageSensories(state = state, onEvent = onEvent)
        }
    }
}





