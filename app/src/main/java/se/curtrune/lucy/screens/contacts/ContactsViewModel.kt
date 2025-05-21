package se.curtrune.lucy.screens.contacts

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.Contact
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.util.Logger

class ContactsViewModel : ViewModel() {
    private var items: List<Item> = emptyList()
    private val contactsModule = LucindaApplication.appModule.contactsModule
    private val _state = MutableStateFlow<ContactsState>(ContactsState())
    val state = _state.asStateFlow()
    private val mutableError = MutableLiveData<String>()
    init {
        println("ContactsViewModel.init{}")
        items = contactsModule.getContacts()
        _state.update { it.copy(
            contacts = items
        ) }
    }

    fun addContact(contact: Contact?, context: Context?) {
        Logger.log("...addContact(Contact, Context")
        val intent = Intent(ContactsContract.Intents.Insert.ACTION)
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
    }
    val error: LiveData<String>
        get() = mutableError

    fun insertContact(contact: Contact, context: Context) {
        Logger.log("...insertContact(Contact, Context)")
        if (contact.displayName.isEmpty()) {
            mutableError.value = "contact name is required"
            return
        }
        val intent = Intent(ContactsContract.Intents.Insert.ACTION)
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
        //email address
        if (contact.hasEmailAddress()) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.email)
        }
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.displayName)
        if (contact.hasPhoneNumber()) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.phoneNumber)
        }
        context.startActivity(intent)
    }
    fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.PermissionGranted -> { permissionGranted(event.granted)}
        }
    }

    private fun getAddress(context: Context, contactId: Int) {
        Logger.log("ContactsViewModel.getAddress(Context, int) contactID", contactId)
        val postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI
        val postal_cursor = context.contentResolver.query(
            postal_uri,
            null,
            ContactsContract.Data.CONTACT_ID + "=" + contactId,
            null,
            null
        )
        while (postal_cursor!!.moveToNext()) {
            val streetIndex =
                postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)
            if (streetIndex > 0) {
                val Strt = postal_cursor.getString(streetIndex)
            }
            //String Cty = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            //String cntry = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
        }
        postal_cursor.close()
    }
    private fun permissionGranted(granted: Boolean) {
        println("ContactsViewModel.permissionGranted($granted)")

    }
}
