package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.ItemStatistics;

public class ItemStatisticsDialog extends BottomSheetDialogFragment {
    private ItemStatistics itemStatistics;
    private Button buttonOK;
    public ItemStatisticsDialog(ItemStatistics statistics){
        log("ItemStatisticsDialog(ItemStatistics)");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_statistics_dialog, container, false);
        initViews(view);
        return view;
    }
    private void initViews(View view){
        buttonOK = view.findViewById(R.id.itemStatisticsDialog_buttonOK);
    }
}
