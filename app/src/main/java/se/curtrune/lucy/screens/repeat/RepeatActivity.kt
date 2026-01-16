package se.curtrune.lucy.screens.repeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.dialogs.RepeatDialog
import se.curtrune.lucy.screens.repeat.composables.RepeatList

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
            val state = viewModel.state.collectAsState()
            LucyTheme {
                RepeatList(repeats = state.value.repeats)
                if (showDialog) {
                    RepeatDialog(onDismiss = {
                        println("onDismiss")
                        showDialog = false
                    }, onConfirm = {})
                }
            }
        }
    }
}


