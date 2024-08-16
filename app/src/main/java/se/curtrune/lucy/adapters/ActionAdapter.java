package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Action;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.workers.DurationWorker;


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
        holder.textViewValue.setText(action.getValue());
        if( action.hasColor()){
            holder.cardView.setCardBackgroundColor(action.getColor());
        }
    }
    public static List<Action> getActionList(Item item, Context context){
        if( VERBOSE) log("ActionAdapter.getActionList(Item)");
        Action actionTime = new Action(context.getString(R.string.time));
        LocalTime targetTime = item.getTargetTime();
        if( targetTime != null) {
            actionTime.setValue(targetTime.toString());
        }
        actionTime.setType(Action.Type.TIME);

/*        Action actionTags = new Action(context.getString(R.string.tags));
        actionTags.setValue(item.getTags());
        actionTags.setType(Action.Type.TAGS);*/

        Action notification = new Action(context.getString(R.string.notification));
        if(item.hasNotification()){
            notification.setValue(item.getNotification().toString());
        }
        notification.setTitle(context.getString(R.string.notification));
        notification.setType(Action.Type.NOTIFICATION);

        Action dateAction = new Action(context.getString(R.string.date));
        if( item.getTargetDate() != null) {
            dateAction.setValue(item.getTargetDate().toString());
        }
        dateAction.setType(Action.Type.DATE);

        Action repeat = new Action(context.getString(R.string.repeat));
        if( item.hasPeriod()){
            repeat.setValue(item.getPeriod().toString());
        }
        repeat.setType(Action.Type.REPEAT);

        Action categoryAction = new Action(context.getString(R.string.category));
        String category = item.getCategory();
        if( category != null && !category.isEmpty()){
            categoryAction.setValue(category);
        }
        categoryAction.setType(Action.Type.CATEGORY);

        Action actionDuration = new Action(context.getString(R.string.duration));
        if( item.isDone()){
            actionDuration.setValue(Converter.formatSecondsWithHours(item.getDuration()));
        }else{
            long estimatedDuration = DurationWorker.getEstimatedDuration(item, context);
            actionDuration.setValue(Converter.formatSecondsWithHours(estimatedDuration));
        }
        actionDuration.setType(Action.Type.DURATION);

        Action actionMental = new Action(context.getString(R.string.mental));
        actionMental.setType(Action.Type.MENTAL);

        Action actionColor = new Action("color");
        actionColor.setType(Action.Type.COLOR);
        if( item.hasColor()){
            actionColor.setColor(item.getColor());
            actionColor.setValue(String.valueOf(item.getColor()));
        }

        ArrayList<Action> actionList = new ArrayList<>();
        actionList.add(actionTime);
        actionList.add(dateAction);
        actionList.add(repeat);
        actionList.add(notification);
        actionList.add(categoryAction);
        actionList.add(actionDuration);
        actionList.add(actionMental);
        actionList.add(actionColor);
        return actionList;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitle;
        private final TextView textViewValue;
        private final CardView cardView;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.actionAdapter_title);
            textViewValue = itemView.findViewById(R.id.actionAdapter_value);
            cardView = itemView.findViewById(R.id.actionAdapter_cardView);
            LinearLayout layout = itemView.findViewById(R.id.actionAdapter_layout);
            layout.setOnClickListener(view -> callback.onAction(items.get(getAdapterPosition())));
        }
    }
}
