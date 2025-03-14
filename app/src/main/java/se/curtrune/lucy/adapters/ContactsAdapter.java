package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Contact;
import se.curtrune.lucy.classes.item.Item;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private List<Item> items;
    public static boolean VERBOSE = false;

    public void setList(List<Item> items) {
        if( VERBOSE) log("ItemAdapter.setList(List<Item>) size", items.size());
        this.items = items;
        notifyDataSetChanged();
    }

    public void sort() {
        if(VERBOSE)log("ItemAdapter.sort()");
        items.sort(Comparator.comparingLong(Item::compare));
        Collections.reverse(items);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(Item item);
        void onLongClick(Item item);
        void onCheckboxClicked(Item item, boolean checked);
    }
    private Callback callback;

    public ContactsAdapter(List<Item> items, Callback callback) {
        if( items == null){
            log("...ItemAdapter called with null list, creating and empty list");
            items = new ArrayList<>();
        }
        if( VERBOSE) log("ItemAdapter(List<Item>, Callback) items size", items.size());
        this.items = items;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ItemAdapter.onCreateViewHolder(...)");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("ItemAdapter.onBindViewHolder() position", position);
        Item item = items.get(position);
        Contact contact = item.getContact();
        assert contact != null;
        holder.textViewDisplayName.setText(contact.getDisplayName());
        holder.textViewPhoneNumber.setText(contact.getPhoneNumber());
        holder.textViewID.setText(String.valueOf(contact.getId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void insert(Item item){
        items.add(0, item);
        notifyItemInserted(0);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewDisplayName;
        private final TextView textViewPhoneNumber;
        private final TextView textViewID;
        //private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDisplayName = itemView.findViewById(R.id.contactsAdapter_displayName);
            textViewPhoneNumber = itemView.findViewById(R.id.contactsAdapter_phoneNumber);
            textViewID = itemView.findViewById(R.id.contactsAdapter_id);
            ConstraintLayout rootLayout = itemView.findViewById(R.id.contactsAdapter_rootLayout);
            rootLayout.setOnClickListener(view->{
                callback.onItemClick(items.get(getAdapterPosition()));
            });
        }
    }
}
