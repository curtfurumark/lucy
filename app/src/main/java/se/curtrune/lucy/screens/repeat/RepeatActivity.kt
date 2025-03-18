package se.curtrune.lucy.screens.repeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.composables.RepeatDialog
import se.curtrune.lucy.persist.SqliteLocalDB
import se.curtrune.lucy.screens.repeat.composables.RepeatInfo
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


