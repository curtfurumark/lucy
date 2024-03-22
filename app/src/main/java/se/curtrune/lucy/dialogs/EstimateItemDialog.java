package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.classes.EstimateDate;


public class EstimateItemDialog extends BottomSheetDialogFragment {

    private TextView textViewDate;
    private TextView textViewEnergyEstimate;
    private TextView textViewDurationEstimate;
    private Button buttonDismiss;
    private Button buttonSave;
    private enum EditMode{
        CREATE, EDIT
    }
    public interface Callback{
        void onEstimate(Estimate estimate);
    }
    private Callback callback;
    public EstimateItemDialog(){
        log("EstimateItemDialog()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EstimateDateDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.estimate_item_dialog, container, false);
        initComponents(view);
       //initDefaults();
        initListeners();
        //setUserInterface(new EstimateDate(date));
        return view;
    }
    private Estimate getEstimate(){
        return new Estimate();
    }

    private void initComponents(View view){
        log("...initComponents(View)");
        //editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonSave = view.findViewById(R.id.estimateItemDialog_buttonSave);
        buttonDismiss = view.findViewById(R.id.estimateItemDialog_buttonDismiss);
        //textViewDurationEstimate = view.findViewById(R.id.estimateDateDialog_duration);
        //textViewEnergyEstimate = view.findViewById(R.id.estimateDateDialog_energy);

    }

    private void initListeners(){
        log("...initListeners()");
        buttonDismiss.setOnClickListener(view1 -> {
            log("...dismiss please");
            dismiss();
        });
        buttonSave.setOnClickListener(view->{
            log("...save please");
            callback.onEstimate(getEstimate());
            dismiss();
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void setUserInterface(EstimateDate estimate){
        log("...setUserInterface(EstimateDate)");
        textViewDate.setText(estimate.getDate().toString());
        String strEnergyEstimate = String.format(Locale.getDefault(), "%d", estimate.getEnergyEstimate());
        textViewEnergyEstimate.setText(strEnergyEstimate);
        String strDurationEstimate = String.format(Locale.getDefault(), "%d", estimate.getDurationEstimate());
        textViewDurationEstimate.setText(strDurationEstimate);
    }
}
