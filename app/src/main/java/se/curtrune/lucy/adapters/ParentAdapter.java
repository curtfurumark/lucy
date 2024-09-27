package se.curtrune.lucy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;

import java.util.List;


public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentHolder> {

    private List<CalenderDate> dateItems;
    private RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    public ParentAdapter(List<CalenderDate> dateItems) {
        this.dateItems = dateItems;
    }

    @NonNull
    @Override
    public ParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_week_adapter, parent, false);
        return new ParentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentHolder holder, int position) {
        CalenderDate dateItem =  dateItems.get(position);
        holder.textViewDate.setText(dateItem.getDate().toString());
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext());
        linearLayoutManager.setInitialPrefetchItemCount(dateItem.getItems().size());
        ItemAdapter itemAdapter = new ItemAdapter(dateItem.getItems(), new ItemAdapter.Callback() {
            @Override
            public void onItemClick(Item item) {

            }

            @Override
            public void onLongClick(Item item) {

            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {

            }
        });
        holder.childRecycler.setAdapter(itemAdapter);
        holder.childRecycler.setLayoutManager(linearLayoutManager);
        holder.childRecycler.setRecycledViewPool( recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return dateItems.size();
    }

    public static class ParentHolder extends RecyclerView.ViewHolder{
        private TextView textViewDate;
        private RecyclerView childRecycler;

        public ParentHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.calenderWeekAdapter_textViewDate);
            childRecycler = itemView.findViewById(R.id.calenderWeekAdapter_recycler);
        }
    }
}
