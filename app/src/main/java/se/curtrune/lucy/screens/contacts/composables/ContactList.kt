package se.curtrune.lucy.screens.contacts.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.curtrune.lucy.screens.contacts.ContactsEvent
import se.curtrune.lucy.screens.contacts.ContactsState

@Composable
fun ContactList(state: ContactsState, onEvent: (ContactsEvent) -> Unit, innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        items(state.contacts) { item ->
            ContactCard(contact = item.contact!!)
            //TODO, null assertion
        }
    }
}