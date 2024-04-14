package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.fragments.TopTenFragment;


public class TopTenAdapter extends RecyclerView.Adapter<TopTenAdapter.MyViewHolder>{
    private List<Mental> items;
    public boolean VERBOSE = false;
    private TopTenFragment.Mode mode;
    private Context context;

    public void setList(List<Mental> items, TopTenFragment.Mode mode) {
        if( VERBOSE) log("ListableAdapter.setList(List<Listable>) size", items.size());
        if( items == null){
            log("ListableAdapter.setList(List<Item>) called with null items");
            return;
        }
        this.items = items;
        this.mode = mode;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(Listable item);
        void onLongClick(Listable item);
    }
    private final Callback callback;

    public TopTenAdapter(List<Mental> items, Callback callback) {
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
        final Mental mental = items.get(position);
        holder.textViewHeading.setText(mental.getHeading());
        context = holder.textViewInfo.getContext();
        String info = "";
        switch(mode){
            case ENERGY:
                info = String.format(Locale.ENGLISH ,"%s %d, %s", context.getString(R.string.energy),mental.getEnergy(), mental.getDate().toString());
                break;
            case ANXIETY:
                info = String.format(Locale.ENGLISH, "%s %d, %s", context.getString(R.string.anxiety),mental.getAnxiety(), mental.getDate().toString());
                break;
            case STRESS:
                info = String.format(Locale.ENGLISH, "%s %d, %s", context.getString(R.string.stress),mental.getStress(), mental.getDate().toString());
                break;
            case MOOD:
                info = String.format(Locale.getDefault(),"%s %d, %s", context.getString(R.string.mood),mental.getMood(), mental.getDate().toString());
                break;

        }
        holder.textViewInfo.setText(info);
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
            ConstraintLayout parentLayout = itemView.findViewById(R.id.listableAdapter_rootLayout);
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
