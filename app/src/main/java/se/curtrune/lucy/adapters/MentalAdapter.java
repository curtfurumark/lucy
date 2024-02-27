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
import se.curtrune.lucy.util.Constants;


public class MentalAdapter extends RecyclerView.Adapter<MentalAdapter.MyViewHolder>{
    private List<Mental> items;
    public boolean VERBOSE = false;
    public enum Mode{
        MOOD, ENERGY, ANXIETY, STRESS
    }
    private Mode mode = Mode.ENERGY;
    public interface Callback{
        void onItemClick(Mental item);
        void onItemLongClick(Mental item);
    }
    private final Callback callback;

    public MentalAdapter(List<Mental> items, Callback callback) {
        if( VERBOSE) log("MentalAdapter(List<Mental>, Context, Callback");
        if (items == null){
            log("...items is null");
        }
        this.items = items;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("MentalAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mental_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("MentalAdapter.onBindViewHolder() mode", mode.toString());
        final Mental item = items.get(position);
        log(item);
        switch (mode){
            case MOOD:
                holder.seekBar.setProgress(item.getMood() + Constants.MOOD_OFFSET);
                break;
            case ENERGY:
                holder.seekBar.setProgress(item.getEnergy() +Constants.ENERGY_OFFSET);
                break;
            case ANXIETY:
                holder.seekBar.setProgress(item.getAnxiety() + Constants.ANXIETY_OFFSET);
                break;
            case STRESS:
                holder.seekBar.setProgress(item.getStress() + Constants.STRESS_OFFSET);
                break;
        }
        holder.textViewHeading.setText(item.getHeading());
        holder.textViewLabel.setText(item.getLabel());
    }


    @Override
    public int getItemCount() {
        return items != null? items.size(): 0;
    }
    public List<Mental> getItems(){
        return items;
    }

    public void setList(List<Mental> items) {
        log("MentalAdapter.setList(List<Mental>) size",  items.size());
        if( items == null){
            log("ItemsAdapter.setList(List<Item>) called with null items");
            return;
        }
        this.items = items;
        notifyDataSetChanged();
    }
    public void show(Mode mode){
        log("MentalAdapter.show(Mode)", mode.toString());
        this.mode = mode;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final SeekBar seekBar;
        private TextView textViewLabel;
        private TextView textViewHeading;


        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);

            textViewHeading = itemView.findViewById(R.id.mentalAdapter_heading);
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
