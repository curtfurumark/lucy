package se.curtrune.lucy.screens.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Contact
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.dialogs.ContactDialog
import se.curtrune.lucy.screens.contacts.composables.ContactsScreen
import se.curtrune.lucy.screens.main.MainViewModel
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
                    ContactsScreen(state = state.value, onEvent = contactsViewModel::onEvent)
                }
            }
        }
    }
    private fun showAddContactDialog() {
        Logger.log("...showAddContactDialog()")
        val dialog = ContactDialog { contact ->
            Logger.log("...onSave(Contact)", contact.toString())
            Logger.log(contact)
            if (contact.displayName.isEmpty()) {
                Toast.makeText(context, "name is required", Toast.LENGTH_LONG).show()
            } else {
                Logger.log("will insert contact")
                //contactsViewModel!!.insertContact(contact, requireContext())
            }
        }
        dialog.show(childFragmentManager, "add contact")
    }
}