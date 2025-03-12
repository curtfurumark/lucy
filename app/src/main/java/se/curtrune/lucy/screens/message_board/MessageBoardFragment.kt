package se.curtrune.lucy.screens.message_board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Message
import se.curtrune.lucy.dialogs.MessageDialog
import se.curtrune.lucy.screens.message_board.composables.MessageBoardScreen
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.InternetWorker
import java.util.stream.Collectors

/**
 * A simple [Fragment] subclass.
 * Use the [MessageBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessageBoardFragment : Fragment(), OnTabSelectedListener {
    // TODO: Rename and change types of parameters
    private var addMessageButton: FloatingActionButton? = null
    private var recyclerMessages: RecyclerView? = null
    private val messages: List<Message>? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireActivity()).apply {
            setContent{
                val messageViewModel = viewModel<MessageBoardViewModel>()
                val state = messageViewModel.state.collectAsState()
                LucyTheme {
                    MessageBoardScreen(state = state.value, onEvent = { event ->
                        messageViewModel.onEvent(event)
                    })
                }
            }
        }
    }

    private fun initListeners() {
        Logger.log("...initListeners()")
        addMessageButton!!.setOnClickListener { view: View? -> showMessageDialog() }
        tabLayout!!.addOnTabSelectedListener(this)
        tabMessages!!.select()
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

    /*    private void selectMessages(){
        log("...selectMessages()");
        if(!InternetWorker.isConnected(getContext())){
            log("...not connected to the internet, cannot get messages");
            Toast.makeText(getContext() , requireContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            return;
        }
        MessageWorker.selectMessages(new MessageWorker.OnMessagesSelected() {
            @Override
            public void onMessages(List<Message> messageList) {
                log("...onMessages(List<Message>) size",messageList.size() );
                messages = messageList;
                setUserInterface(messages);
            }

            @Override
            public void onError(String message) {
                log("...onError(String message)");
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }*/
    private fun observe() {
/*        messageBoardViewModel!!.messages.observe(
            requireActivity()
        ) { value ->
            Logger.log("...observe messages ")
            value.forEach(Consumer { x: Message? -> println(x) })
            adapter!!.setList(messages)
        }*/
    }

    private fun selectTab(category: String) {
        Logger.log("...selectTab()", category)
        if (category == "bug") {
            tabBugs!!.select()
        } else if (category == "suggestion") {
            tabSuggestions!!.select()
        } else if (category == "wip") {
            tabWip!!.select()
        } else if (category == "message") {
            tabMessages!!.select()
        } else {
            Logger.log("WARNING NO SUCH TAB")
        }
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
            messageBoardViewModel!!.insert(message, context)
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
        val dialog = MessageDialog(message)
        dialog.setCallback { message, mode ->
            Logger.log("...onMessage updated(Message, Mode)")
            messageBoardViewModel!!.update(message, context)
        }
        dialog.show(childFragmentManager, "edit message")
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