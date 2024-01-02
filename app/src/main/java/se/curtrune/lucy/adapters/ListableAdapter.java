package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Listable;


public class ListableAdapter extends RecyclerView.Adapter<ListableAdapter.MyViewHolder>{
    private List<Listable> items;
    public boolean VERBOSE = false;

    public void setList(List<Listable> items) {
        if( VERBOSE) log("ListableAdapter.setList(List<Listable>) size", items.size());
        if( items == null){
            log("ListableAdapter.setList(List<Item>) called with null items");
            return;
        }
        this.items = items;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(Listable item);
        void onLongClick(Listable item);
    }
    private final Callback callback;

    public ListableAdapter(List<Listable> items, Callback callback) {
        if( VERBOSE) log("ListableAdapter(List<Listable>, Context, Callback)");
        if (items == null){
            log("...taskList is null");
        }
        this.items = items;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ListableAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listable_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("ItemsAdapter.onBindViewHolder();");
        final Listable item = items.get(position);
        holder.textViewHeading.setText(item.getHeading());
        //TODO rename textView
        holder.textViewInfo.setText(item.getInfo());
    }


    @Override
    public int getItemCount() {
        return items != null? items.size(): 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewHeading;
        private final TextView textViewInfo;

        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);
            textViewHeading = itemView.findViewById(R.id.listableAdapter_heading);
            textViewInfo = itemView.findViewById(R.id.listableAdapter_info);
            ConstraintLayout parentLayout = itemView.findViewById(R.id.constraintLayout_taskAdapter);
            parentLayout.setOnClickListener(view -> {
                if( callback != null){
                    callback.onItemClick(items.get(getAdapterPosition()));
                }
            });
            parentLayout.setOnLongClickListener(e->{
                callback.onLongClick(items.get((getAdapterPosition())));
                return true;
            });
        }
    }

}
