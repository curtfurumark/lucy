package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.EstimateItems;
import se.curtrune.lucy.classes.Item;


public class EstimateItemsDialog extends BottomSheetDialogFragment {
    private TextView textViewEnergy;
    private SeekBar seekBarEnergy;
    private TextView textViewDuration;
    private Button buttonDismiss;
    private List<Item> items;

    private int energy;
    public EstimateItemsDialog(List<Item> items){
        this.items = items;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EstimateDateDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.estimate_items_dialog, container, false);
        initComponents(view);
        initListeners();
        setUserInterface(new EstimateItems(items));
        return view;
    }


    private void initComponents(View view){
        log("...initComponents(View)");
        //editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonDismiss = view.findViewById(R.id.estimateItemsDialog_buttonOK);
        textViewDuration = view.findViewById(R.id.estimateItemsDialog_duration);
        textViewEnergy = view.findViewById(R.id.estimateItemsDialog_labelEnergy);
        seekBarEnergy = view.findViewById(R.id.estimateItemsDialog_seekBarEnergy);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonDismiss.setOnClickListener(view->dismiss());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }


    private void setUserInterface(EstimateItems estimate){
        log("...setUserInterface(EstimateDate)");
        //textViewDate.setText(estimate.getDate().toString());
        String strEnergyEstimate = String.format(Locale.getDefault(), "%d", estimate.getEnergyEstimate());
        textViewEnergy.setText(strEnergyEstimate);
        String strDurationEstimate = String.format(Locale.getDefault(), "%d", estimate.getDurationEstimate());
        textViewDuration.setText(strDurationEstimate);
    }
}
