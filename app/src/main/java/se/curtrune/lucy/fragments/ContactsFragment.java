package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ContactsAdapter;
import se.curtrune.lucy.classes.Contact;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Media;
import se.curtrune.lucy.classes.Type;


public class ContactsFragment extends Fragment {

    private ActivityResultLauncher<String> activityResultLauncher;
    private RecyclerView recyclerContacts;
    private FloatingActionButton buttonAddContact;

    private ContactsAdapter contactsAdapter;
    public ContactsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("ContactsFragment.onCreateView(...)");
        View view =  inflater.inflate(R.layout.contacts_fragment, container, false);
        initViews(view);
        initViewModel();
        initRecycler();
        pleaseListContacts();
        return view;
    }
    private void pleaseListContacts(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED){
            log("not permitted to read contacts, will ask for permission");
            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean granted) {
                    log("...onActivityResult(Boolean)", granted);
                    if( granted){
                        listContacts();
                    }else{
                        Toast.makeText(getContext(), "permission contacts denied", Toast.LENGTH_LONG).show();
                    }
                }
            });
            activityResultLauncher.launch(Manifest.permission.READ_CONTACTS);
        }else{
            listContacts();
        }

    }
    private void initRecycler(){
        log("...initRecycler()");
        contactsAdapter = new ContactsAdapter(new ArrayList<>(), new ContactsAdapter.Callback() {
            @Override
            public void onItemClick(Item item) {
                log("...onItemClick(Item)");
                Toast.makeText(getContext(), item.getContact().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(Item item) {

            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {

            }
        });
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContacts.setItemAnimator(new DefaultItemAnimator());
        recyclerContacts.setAdapter(contactsAdapter);

    }
    private void initViewModel(){

    }
    private void initViews(View view){
        log("...initViews(View)");
        recyclerContacts = view.findViewById(R.id.contactsFragment_recyclerContacts);
    }
    private void listContacts(){
        log("...listContacts()");
        List<Item> contactItems = new ArrayList<>();
        ContentResolver contentResolver = requireActivity().getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if( cursor.getCount() > 0 ){
            while(cursor.moveToNext()){
                String columnNameDisplayName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
                String idColumnName = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
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
            }
        }else{
            log("NO CONTACTS FOUND ON THIS DEVICE");
        }
        contactsAdapter.setList(contactItems);
    }
    private Item createContactItem(String displayName, String phoneNumber, long id){
        log("...createContactItem");
        Item item = new Item();
        item.setType(Type.CONTACT);
        Contact contact = new Contact();
        contact.setDisplayName(displayName);
        contact.setPhoneNumber(phoneNumber);
        contact.setId(id);
        item.setContact(contact);
        return item;
    }

}