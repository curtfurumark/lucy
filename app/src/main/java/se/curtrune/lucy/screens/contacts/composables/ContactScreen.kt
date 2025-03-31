package se.curtrune.lucy.screens.contacts.composables

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import se.curtrune.lucy.screens.contacts.ContactsEvent
import se.curtrune.lucy.screens.contacts.ContactsState

@Composable
fun ContactsScreen(state: ContactsState, onEvent: (ContactsEvent) -> Unit) {
    val context = LocalContext.current
    //val contacts by contactsViewModel.contacts.observeAsState(initial = emptyList())
    //val showDialog by contactsViewModel.showDialog.observeAsState(initial = false)
/*    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    contactsViewModel.onEvent(ContactsEvent.PermissionGranted(true))
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        )*/
    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onEvent(ContactsEvent.PermissionGranted(true))
        } else {
            println("request permission not granted")
            //permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
    Scaffold(
    ) { innerPadding ->
        ContactList(state = state, innerPadding = innerPadding, onEvent = onEvent)
    }
}