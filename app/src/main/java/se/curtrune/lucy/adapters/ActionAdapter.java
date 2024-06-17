package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Action;


public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder>{
    private List<Action> items;
    public static boolean VERBOSE = true;


    public interface Callback{
        void onAction(Action action);

    }
    private final Callback callback;

    public ActionAdapter(List<Action> items, Callback callback) {
        if( VERBOSE) log("ActionAdapter(List<Action>, Callback) items size", items.size());
        this.items = items;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ActionAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("ActionAdapter.onBindViewHolder() position", position);
        Action action = items.get(position);


        holder.textViewTitle.setText(action.getTitle());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitle;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.actionAdapter_title);
            textViewTitle.setOnClickListener(view -> {
                callback.onAction(items.get(getAdapterPosition()));
            });
        }
    }
}
