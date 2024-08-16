package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
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

    public ItemAdapter(List<Item> items, Callback callback) {
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
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("ItemAdapter.onBindViewHolder() position", position);
        Item item = items.get(position);
        holder.textView_heading.setText(item.getHeading());
        holder.setIsRecyclable(false);
        holder.checkBox_state.setChecked(item.getState().equals(State.DONE));
        if( item.hasNotification()){
            holder.imageViewNotificationIcon.setVisibility(View.VISIBLE);
        }
        if( item.hasPeriod()){
            holder.imageViewRepeatIcon.setVisibility(View.VISIBLE);
        }
        if(item.isPrioritized()){
            holder.imageViewImportantIcon.setVisibility(View.VISIBLE);
        }
        if( item.hasColor()){
            holder.cardView.setCardBackgroundColor(item.getColor());
        }
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
        private final TextView textView_heading;
        private final CheckBox checkBox_state;
        private final ImageView imageViewRepeatIcon;
        private final ImageView imageViewNotificationIcon;
        private final ImageView imageViewImportantIcon;
        private final CardView cardView;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textView_heading = itemView.findViewById(R.id.itemAdapter_itemHeading);
            cardView = itemView.findViewById(R.id.itemAdapter_cardView);
            checkBox_state = itemView.findViewById(R.id.itemAdapter_itemState);
            imageViewNotificationIcon = itemView.findViewById(R.id.itemAdapter_notificationIcon);
            imageViewRepeatIcon = itemView.findViewById(R.id.itemAdapter_repeatIcon);
            imageViewImportantIcon = itemView.findViewById(R.id.itemAdapter_importantIcon);
            checkBox_state.setOnClickListener(view -> {
                //log("...on checkbox state");
                Item item = items.get(getAdapterPosition());
                callback.onCheckboxClicked(items.get(getAdapterPosition()), checkBox_state.isChecked());});
            ConstraintLayout parentLayout = itemView.findViewById(R.id.constraintLayout_todayAdapter);
            parentLayout.setOnClickListener(view -> {
                callback.onItemClick(items.get(getAdapterPosition()));
            });
            parentLayout.setOnLongClickListener(e->{
                callback.onLongClick(items.get((getAdapterPosition())));
                return true;
            });
        }
    }
}
