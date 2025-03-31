package se.curtrune.lucy.screens.contacts

import se.curtrune.lucy.classes.Contact
import se.curtrune.lucy.classes.item.Item

data class ContactsState(
    val contacts: List<Item> = emptyList(),
)
