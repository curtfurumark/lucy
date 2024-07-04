package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Action;
import se.curtrune.lucy.classes.Item;


public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder>{
    private final List<Action> items;
    public static boolean VERBOSE = false;


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

        //holder.textViewTitle.setCompoundDrawables();

    }
    public static List<Action> getActionList(Item item, Context context){
        log("ActionAdapter.getActionList(Item)");
        Action time = new Action();
        time.setTitle(context.getString(R.string.time));
        time.setType(Action.Type.TIME);

        Action notification = new Action();
        notification.setTitle(context.getString(R.string.notification));
        notification.setType(Action.Type.NOTIFICATION);

        Action dateAction = new Action();
        if( item.getTargetDate() != null) {
            dateAction.setTitle(item.getTargetDate().toString());
        }
        dateAction.setType(Action.Type.DATE);

        Action repeat = new Action();
        repeat.setTitle(context.getString(R.string.repeat));
        repeat.setType(Action.Type.REPEAT);

        Action categoryAction = new Action();
        String category = item.getCategory();
        if( category != null && !category.isEmpty()){
            categoryAction.setTitle(category);
        }else {
            categoryAction.setTitle(context.getString(R.string.category));
        }
        categoryAction.setType(Action.Type.CATEGORY);

        Action actionDuration = new Action();
        actionDuration.setTitle(context.getString(R.string.duration));
        actionDuration.setType(Action.Type.DURATION);

        Action actionMental = new Action();
        actionMental.setTitle(context.getString(R.string.mental));
        actionMental.setType(Action.Type.MENTAL);

        ArrayList<Action> actionList = new ArrayList<>();
        actionList.add(time);
        actionList.add(dateAction);
        actionList.add(repeat);
        actionList.add(notification);
        actionList.add(categoryAction);
        actionList.add(actionDuration);
        actionList.add(actionMental);
        return actionList;
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
            textViewTitle.setOnClickListener(view -> callback.onAction(items.get(getAdapterPosition())));
        }
    }
}
