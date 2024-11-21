package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.LucindaInfo;

public class DevActivityAdapter extends RecyclerView.Adapter<DevActivityAdapter.ViewHolder>{
    private List<LucindaInfo> lucindaInfoList;
    public DevActivityAdapter(List<LucindaInfo> lucindaInfoList ){
        log("DevActivityAdapter(List<LucindaInfo>))");
        this.lucindaInfoList = lucindaInfoList;
    }
    @NonNull
    @Override
    public DevActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dev_adapter, parent, false);
        return new DevActivityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DevActivityAdapter.ViewHolder holder, int position) {
        LucindaInfo lucindaInfo = lucindaInfoList.get(position);
        holder.textViewKey.setText(lucindaInfo.getKey());
        holder.textViewValue.setText(lucindaInfo.getValue());

    }

    @Override
    public int getItemCount() {
        return lucindaInfoList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewKey;
        private TextView textViewValue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewKey = itemView.findViewById(R.id.devAdapter_textViewKey);
            textViewValue = itemView.findViewById(R.id.devAdapter_textViewValue);
        }
    }
}
