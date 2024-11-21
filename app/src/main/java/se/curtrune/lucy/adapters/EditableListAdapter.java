package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;

public class EditableListAdapter extends RecyclerView.Adapter<EditableListAdapter.ViewHolder>{
    private List<Item> items;
    private int currentItem = 0;
    public static boolean VERBOSE = false;

    public void setList(List<Item> items) {
        if( VERBOSE) log("EditableListAdapter.setList(List<Item>) size", items.size());
        this.items = items;
        notifyDataSetChanged();
    }


    public interface Callback{
        void onNewLine(String  heading, int position);
        void onHeadingChanged(String heading, int position);
    }
    private Callback callback;

    public EditableListAdapter(List<Item> items, Callback callback) {
        if( items == null){
            log("...EditableListAdapter called with null list, creating and empty list");
            items = new ArrayList<>();
        }
        if( VERBOSE) log("EditableListAdapter(List<Item>, Callback) items size", items.size());
        this.items = items;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("EditableListAdapter.onCreateViewHolder(...)");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.editable_list_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("EditableListAdapter.onBindViewHolder() position", position);
        if( currentItem == position){
            holder.editTextListItem.requestFocus();
        }
        Item item  = items.get(position);
        if(item.getHeading().isEmpty()){
            holder.editTextListItem.setHint("type here");
            //log("IS THIS THE OFFENDING ONE");
        }else {
            holder.editTextListItem.setText(getBulletSpannableString(item.getHeading()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private SpannableString getBulletSpannableString(String itemHeading) {
        SpannableString spannableString = new SpannableString(itemHeading);
        spannableString.setSpan(new BulletSpan(), 0, itemHeading.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public void setFocus(int position){
        currentItem = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final EditText editTextListItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextListItem = itemView.findViewById(R.id.itemAdapter_editText);
            editTextListItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if( VERBOSE) log("...beforeTextChanged(...)", s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(VERBOSE)log("...onTextChanged(...)", s.toString());
                    callback.onHeadingChanged(s.toString(), getAdapterPosition());

                }

                @Override
                public void afterTextChanged(Editable s) {
                    log("...afterTextChanged(Editable)", s.toString());
                    if( s.toString().endsWith("\n")){
                        log("...gotOurselves a new line");
                        String item = s.toString().replace("\n", "");
                        notifyItemChanged(getAdapterPosition());
                        callback.onNewLine(item, getAdapterPosition());
                    }
                }
            });

        }
    }
}
