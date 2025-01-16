package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.screens.util.Converter;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private List<Message> messages;
    public static boolean VERBOSE = false;

    public void setList(List<Message> messages) {
        if( VERBOSE) log("ItemAdapter.setList(List<Item>) size", messages.size());
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void sort() {
        if(VERBOSE)log("TodayAdapter.sort()");
/*        items.sort(Comparator.comparingLong(Item::compare));
        Collections.reverse(items);
        notifyDataSetChanged();*/
    }

    public interface Callback{
        void onItemClick(Message message);
    }
    private Callback callback;

    public MessageAdapter(List<Message> messages, Callback callback) {
        if( messages == null){
            log("...MessageAdapter called with null list, creating and empty list");
            messages = new ArrayList<>();
        }
        if( VERBOSE) log("MessageAdapter(List<Item>, Callback) items size", messages.size());
        this.messages = messages;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("MessageAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("ItemAdapter.onBindViewHolder() position", position);
        Message message = messages.get(position);
        holder.textViewSubject.setText(message.getSubject());
        holder.textViewContent.setText(message.getContent());
        holder.textViewUser.setText(message.getUser());
        holder.textViewCreated.setText(Converter.format(message.getCreated()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout parentLayout;
        private final TextView textViewSubject;
        private final TextView textViewContent;
        private final TextView textViewUser;
        private final TextView textViewCreated;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.messageAdapter_subject);
            textViewContent = itemView.findViewById(R.id.messageAdapter_content);
            textViewUser = itemView.findViewById(R.id.messageAdapter_user);
            textViewCreated = itemView.findViewById(R.id.messageAdapter_created);
            parentLayout = itemView.findViewById(R.id.messageAdapter_parentLayout);
            parentLayout.setOnClickListener(view->{
                callback.onItemClick(messages.get(getAdapterPosition()));
            });
        }
    }
}
