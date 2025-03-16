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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.dialogs.MessageDialog
import se.curtrune.lucy.screens.message_board.composables.AddMessageBottomSheet
import se.curtrune.lucy.screens.message_board.composables.MessageBoardScreen
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.InternetWorker

/**
 * A simple [Fragment] subclass.
 * Use the [MessageBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessageBoardFragment : Fragment(), OnTabSelectedListener {
    // TODO: Rename and change types of parameters
    private var addMessageButton: FloatingActionButton? = null
    private var tabLayout: TabLayout? = null
    private var tabMessages: TabLayout.Tab? = null
    private var tabWip: TabLayout.Tab? = null
    private var tabSuggestions: TabLayout.Tab? = null
    private var tabBugs: TabLayout.Tab? = null
    private var messageBoardViewModel: MessageBoardViewModel? = null

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
                        AddMessageBottomSheet(onDismiss = {
                            showAddMessageBottomSheet = false
                        }, onSave = { message->
                            showAddMessageBottomSheet = false
                            messageViewModel.onEvent(MessageBoardEvent.NewMessage(message))
                        })
                    }
                }
            }
        }
    }
    private fun initTabs() {
        Logger.log("...initTabs()")
        tabMessages = tabLayout!!.newTab()
        tabMessages!!.setText("messages")
        tabMessages!!.setTag("message")

        tabBugs = tabLayout!!.newTab()
        tabBugs!!.setText("bugs")
        tabBugs!!.setTag("bug")

        tabSuggestions = tabLayout!!.newTab()
        tabSuggestions!!.setText("suggestions")
        tabSuggestions!!.setTag("suggestion")

        tabWip = tabLayout!!.newTab()
        tabWip!!.setText("wip")
        tabWip!!.setTag("wip")

        tabLayout!!.addTab(tabMessages!!)
        tabLayout!!.addTab(tabBugs!!)
        tabLayout!!.addTab(tabSuggestions!!)
        tabLayout!!.addTab(tabWip!!)
    }


    private fun showMessageDialog() {
        if (!InternetWorker.isConnected(context)) {
            Logger.log("...trying to add a message to message board without internet connection")
            Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                .show()
            return
        }
        val dialog = MessageDialog()
        dialog.setCallback { message, mode ->
            Logger.log(
                "MessageDialog.onMessage(Message, Mode)",
                message.subject
            )
            //messageBoardViewModel!!.insert(message, context)
        }
        dialog.show(childFragmentManager, "add message")
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        Logger.log("...onTabSelected(TabLayout.Tab)", tab.text.toString())
        //log("\t\tmessages size", messages.size());
        //setUserInterface(tab.getTag().toString());
        messageBoardViewModel!!.filter(tab.tag as String?)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    private fun showMessageDialog(message: Message) {
        Logger.log("...showMessageDialog(Message)")
/*        val dialog = MessageDialog(message)
        dialog.setCallback { message, mode ->
            Logger.log("...onMessage updated(Message, Mode)")
            messageBoardViewModel!!.update(message, context)
        }
        dialog.show(childFragmentManager, "edit message")*/
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