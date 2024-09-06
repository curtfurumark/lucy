package se.curtrune.lucy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;

public class MinimalItemAdapter extends RecyclerView.Adapter<MinimalItemAdapter.ViewHolder> {
    private List<Item> items;

    public MinimalItemAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minimal_item_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.textViewHeading.setText(item.getHeading());
        holder.textViewTime.setText(item.getTargetTime().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewHeading;
        private final TextView textViewTime;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeading = itemView.findViewById(R.id.minimalItemAdapter_heading);
            textViewTime = itemView.findViewById(R.id.minimalItemAdapter_time);
            layout = itemView.findViewById(R.id.minimalItemAdapter_constraintLayout);
            //layout.setOnClickListener();
        }
    }
}
