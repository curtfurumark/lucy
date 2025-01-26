package se.curtrune.lucy.screens.contacts;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Contact;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;

public class ContactsViewModel extends ViewModel {
    private List<Item> items;
    private MutableLiveData<String> mutableError = new MutableLiveData<>();
    private MutableLiveData<List<Item>> mutableContacts = new MutableLiveData<>();
    public void init(Context context){
        log("ContactsViewModel.init(Context)");
        items = getContacts(context);
        mutableContacts.setValue(items);
    }
    public void addContact(Contact contact, Context context){
        log("...addContact(Contact, Context");
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

    }
    private Item createContactItem(String displayName, String phoneNumber, long id){
        log("ContactsViewModel.createContactItem");
        Item item = new Item();
        item.setType(Type.CONTACT);
        Contact contact = new Contact();
        contact.setDisplayName(displayName);
        contact.setPhoneNumber(phoneNumber);
        contact.setId(id);
        item.setContact(contact);
        return item;
    }
    public void filterContacts(String filter) {
        log("ContactsViewModel.filterContacts(String)", filter);
        List<Item> filteredList = new ArrayList<>();
        filteredList = items.stream().filter(item-> item.getContact().contains(filter)).collect(Collectors.toList());
        mutableContacts.setValue(filteredList);
    }
    public LiveData<List<Item>> getContacts(){
        return mutableContacts;
    }
    private List<Item> getContacts(Context context){
        log("...getContacts(Context)");
        List<Item> contactItems = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        log("...CONTENT_URI", uri.toString());
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if( cursor.getCount() > 0 ){
            while(cursor.moveToNext()){
                String columnNameDisplayName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
                String idColumnName = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                //Uri uri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String postcode = ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE;
                String phoneNumberColumnName = ContactsContract.CommonDataKinds.Phone.NUMBER;
                int displayNameIndex = cursor.getColumnIndex(columnNameDisplayName);
                int idIndex = cursor.getColumnIndex(idColumnName);
                int phoneNumberIndex =cursor.getColumnIndex(phoneNumberColumnName);
                String name = cursor.getString(displayNameIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                long id = cursor.getLong(idIndex);
                contactItems.add(createContactItem(name,phoneNumber, id));
                log("...displayName", name);
                log("...phoneNumber", phoneNumber);
                log("...id", id);
                log("...post code", postcode);

            }
        }else{
            log("NO CONTACTS FOUND ON THIS DEVICE");
        }
        return contactItems;
    }
    public LiveData<String> getError(){
        return mutableError;
    }

    public void insertContact(Contact contact, Context context) {
        log("...insertContact(Contact, Context)");
        if( contact.getDisplayName().isEmpty()){
            mutableError.setValue("contact name is required");
            return;
        }
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        //email address
        if( contact.hasEmailAddress()) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail());
        }
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contact.getDisplayName());
        if( contact.hasPhoneNumber()) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhoneNumber());
        }
        context.startActivity(intent);
    }
    private void getAddress(Context context, int contactId){
        log("ContactsViewModel.getAddress(Context, int) contactID", contactId);
        Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
        Cursor postal_cursor  = context.getContentResolver().query(postal_uri,null,  ContactsContract.Data.CONTACT_ID + "="+contactId, null,null);
        while(postal_cursor.moveToNext())
        {
            int streetIndex = postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET);
            if( streetIndex > 0) {
                String Strt = postal_cursor.getString(streetIndex);
            }
            //String Cty = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            //String cntry = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
        }
        postal_cursor.close();

    }


}
