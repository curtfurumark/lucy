package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.util.Converter;


public class MentalAdapter extends RecyclerView.Adapter<MentalAdapter.MyViewHolder>{
    private List<Mental> items;
    public boolean VERBOSE = false;
    public enum Mode{
        MOOD, ENERGY
    }
    private Mode mode = Mode.ENERGY;
    public interface Callback{
        void onItemClick(Mental item);
        void onItemLongClick(Mental item);
    }
    private final Callback callback;

    public MentalAdapter(List<Mental> items, Callback callback) {
        if( VERBOSE) log("ItemsAdapter(List<Item>, Context, Callback");
        if (items == null){
            log("...items is null");
        }
        this.items = items;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ItemsAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mental_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("ItemsAdapter.onBindViewHolder();");
        final Mental item = items.get(position);
        switch (mode){
            case MOOD:
                holder.seekBar.setProgress(item.getDepression());
                break;
            case ENERGY:
                holder.seekBar.setProgress(item.getEnergy());
                break;
        }
        holder.textViewLabel.setText(item.getLabel());
    }


    @Override
    public int getItemCount() {
        return items != null? items.size(): 0;
    }

    public void setList(List<Mental> items) {
        log("MentalAdapter.setList(List<Mental>) size",  items.size());
        if( items == null){
            log("ItemsAdapter.setList(List<Item>) called with null items");
            return;
        }
        if( VERBOSE) log("TaskAdapter.setList(List<Task>), size ", items.size());
        this.items = items;
        notifyDataSetChanged();
    }
    public void show(Mode mood){
        this.mode = mode;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final SeekBar seekBar;
        private TextView textViewLabel;


        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);

            textViewLabel = itemView.findViewById(R.id.mentalAdapter_labelSeekbar);
            seekBar = itemView.findViewById(R.id.mentalAdapter_seekbar);
            seekBar.setOnTouchListener((view, motionEvent) -> true);//disable seekbar
            ConstraintLayout parentLayout = itemView.findViewById(R.id.mentalAdapter_layout);
            parentLayout.setOnClickListener(view -> {
                if( callback != null){
                    callback.onItemClick(items.get(getAdapterPosition()));
                }
            });
            parentLayout.setOnLongClickListener(v -> {
                if( callback == null){
                    return false;
                }
                callback.onItemLongClick(items.get(getAdapterPosition()));
                return true;
            });
        }
    }

}
