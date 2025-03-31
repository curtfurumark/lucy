package se.curtrune.lucy.modules

import android.content.Context
import android.provider.ContactsContract
import se.curtrune.lucy.classes.Contact
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.util.Logger

class ContactsModule(private val context: Context) {
    fun getContacts(): List<Item> {
        Logger.log("...getContacts(Context)")
        val contactItems: MutableList<Item> = ArrayList()
        val contentResolver = context.contentResolver
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        Logger.log("...CONTENT_URI", uri.toString())
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val columnNameDisplayName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                val idColumnName = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                //Uri uri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                val postcode = ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE
                val phoneNumberColumnName = ContactsContract.CommonDataKinds.Phone.NUMBER
                val displayNameIndex = cursor.getColumnIndex(columnNameDisplayName)
                val idIndex = cursor.getColumnIndex(idColumnName)
                val phoneNumberIndex = cursor.getColumnIndex(phoneNumberColumnName)
                val name = cursor.getString(displayNameIndex)
                val phoneNumber = cursor.getString(phoneNumberIndex)
                val id = cursor.getLong(idIndex)
                contactItems.add(createContactItem(name, phoneNumber, id))
                Logger.log("...displayName", name)
                Logger.log("...phoneNumber", phoneNumber)
                Logger.log("...id", id)
                Logger.log("...post code", postcode)
            }
        } else {
            Logger.log("NO CONTACTS FOUND ON THIS DEVICE")
        }
        return contactItems
    }
    private fun createContactItem(displayName: String, phoneNumber: String, id: Long): Item {
        Logger.log("ContactsViewModel.createContactItem")
        val item = Item()
        item.type = Type.CONTACT
        val contact = Contact()
        contact.displayName = displayName
        contact.phoneNumber = phoneNumber
        contact.id = id
        item.contact = contact
        return item
    }
}