package se.curtrune.lucy.screens.message_board.composables

import android.os.Build
import android.telephony.TelephonyCallback.MessageWaitingIndicatorListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
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
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.composables.top_app_bar.FlexibleTopBar
import se.curtrune.lucy.composables.top_app_bar.LucindaTopAppBar
import se.curtrune.lucy.modules.TopAppbarModule.topAppBarState
import se.curtrune.lucy.screens.message_board.MessageBoardEvent
import se.curtrune.lucy.screens.message_board.MessageBoardState
import se.curtrune.lucy.screens.message_board.MessageBoardViewModel
import se.curtrune.lucy.screens.message_board.MessageChannel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageBoardScreen(){
    println("MessageBoardScreen()")
    val viewModel: MessageBoardViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var showAddMessageBottomSheet by remember {
        mutableStateOf(false)
    }
    var showProgressBar by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    println("MessageBoardScreen() number of messages ${state.messages.size}")
    LaunchedEffect(viewModel) {
        viewModel.eventFlow.collect{ event->
            println("event $event ")
            when(event){
                is MessageChannel.ShowAddMessageBottomSheet -> {showAddMessageBottomSheet = true}
                is MessageChannel.ShowSnackBar -> {
                    println("snackbar message: ${event.message}")
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                is MessageChannel.ShowProgressBar -> {
                    showProgressBar =event.show
                }
            }
        }
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            FlexibleTopBar(
                scrollBehavior = scrollBehavior,
                content = {
                    LucindaTopAppBar(
                        state = topAppBarState.collectAsState().value,
                        onEvent = { appBarEvent ->
                            println("appBarEvent $appBarEvent")
                            //viewModel.onEvent(appBarEvent)
                        }
                    )
                },
                onEvent = {
                    println("onEvent $it")
                }
            )
        },
        floatingActionButton = { AddItemFab(onAddClick = {
            viewModel.onEvent(MessageBoardEvent.OnAddMessageClick)
        })}
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxWidth().padding(innerPadding)) {
            MessageBoardButton(state = state, onEvent = viewModel::onEvent)
            MessageList(state = state, onEvent = viewModel::onEvent)
        }
    }
    if( showAddMessageBottomSheet){
        AddMessageBottomSheet(
            defaultMessage = state.defaultMessage,
            onDismiss = {
                showAddMessageBottomSheet = false
                viewModel.onEvent(MessageBoardEvent.OnAddMessageDismiss)
            }, onSave = {
                message, mode->
                    if(mode == se.curtrune.lucy.screens.message_board.composables.Mode.CREATE) {
                        viewModel.onEvent(MessageBoardEvent.NewMessage(message))
                    }else{
                        viewModel.onEvent(MessageBoardEvent.UpdateMessage(message))
                    }
                showAddMessageBottomSheet = false
        })
    }
}

