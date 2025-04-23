package se.curtrune.lucy.screens.message_board

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.screens.message_board.composables.AddMessageBottomSheet
import se.curtrune.lucy.screens.message_board.composables.MessageBoardScreen
import se.curtrune.lucy.screens.message_board.composables.Mode
import se.curtrune.lucy.util.Logger

/**
 * A simple [Fragment] subclass.
 * Use the [MessageBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessageBoardFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Logger.log("...getArguments() != null")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent{
                val messageViewModel = viewModel<MessageBoardViewModel>()
                val state = messageViewModel.state.collectAsState()
                var showAddMessageBottomSheet by remember {
                    mutableStateOf(false)
                }
                var showProgressBar by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(messageViewModel) {
                    messageViewModel.eventFlow.collect{ event->
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
                LucyTheme {
                    Scaffold(floatingActionButton = { AddItemFab {
                        messageViewModel.onEvent(MessageBoardEvent.OnAddMessageClick)
                    }}) { padding->
                        MessageBoardScreen(
                            modifier = Modifier.padding(padding),state = state.value, onEvent = { event ->
                            messageViewModel.onEvent(event)
                        })
                    }
                    if(showAddMessageBottomSheet){
                        AddMessageBottomSheet(defaultMessage = state.value.defaultMessage, onDismiss = {
                            showAddMessageBottomSheet = false
                        }, onSave = { message, mode->
                            showAddMessageBottomSheet = false
                            if(mode == Mode.CREATE) {
                                messageViewModel.onEvent(MessageBoardEvent.NewMessage(message))
                            }else{
                                messageViewModel.onEvent(MessageBoardEvent.UpdateMessage(message))
                            }
                        })
                    }
                }
            }
        }
    }


    companion object {
        var VERBOSE: Boolean = false

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): MessageBoardFragment {
            return MessageBoardFragment()
        }
    }
}