package se.curtrune.lucy.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.composables.AddItemFab
import se.curtrune.lucy.screens.contacts.composables.ContactsScreen
import se.curtrune.lucy.util.Logger

class ContactsFragment : Fragment() {
    private var activityResultLauncher: ActivityResultLauncher<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setContent {
                val contactsViewModel  = viewModel<ContactsViewModel>()
                val state = contactsViewModel.state.collectAsState()
                var showAddContactDialog by remember {
                    mutableStateOf(false)
                }
                LucyTheme {
                    Scaffold(
                      floatingActionButton = { AddItemFab {
                          showAddContactDialog = true
                      }}
                    ) { padding ->
                        ContactsScreen(modifier = Modifier.padding(padding), state = state.value, onEvent = contactsViewModel::onEvent)
                    }
                }
            }
        }
    }
}