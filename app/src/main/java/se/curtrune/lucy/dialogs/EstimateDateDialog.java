package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.EstimateItems;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;


public class EstimateDateDialog extends BottomSheetDialogFragment {
    private EditText editTextDays;
    private TextView textViewDate;
    private TextView textViewEnergyEstimate;
    private TextView textViewDurationEstimate;

    private Button buttonDismiss;
    private List<Item> items = new ArrayList<>();
    private LocalDate date;
    private enum EditMode{
        CREATE, EDIT
    }

    private Repeat.Mode periodMode = Repeat.Mode.DAY_OF_WEEKS;
    public EstimateDateDialog(LocalDate date){
        log("EstimateDateDialog(LocalDate)");
        this.date = date;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EstimateDateDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.estimate_date_dialog, container, false);
        initComponents(view);
       //initDefaults();
        initListeners();
        setUserInterface(new EstimateItems(items));
        return view;
    }

    private void initComponents(View view){
        log("...initComponents(View)");
        //editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonDismiss = view.findViewById(R.id.estimateDate_buttonOK);
        textViewDate = view.findViewById(R.id.estimateDateDialog_date);
        textViewDurationEstimate = view.findViewById(R.id.estimateDateDialog_duration);
        textViewEnergyEstimate = view.findViewById(R.id.estimateDateDialog_energy);

    }

    private void initListeners(){
        log("...initListeners()");
        buttonDismiss.setOnClickListener(view1 -> {
            dismiss();
        });
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
        textViewEnergyEstimate.setText(strEnergyEstimate);
        String strDurationEstimate = String.format(Locale.getDefault(), "%d", estimate.getDurationEstimate());
        textViewDurationEstimate.setText(strDurationEstimate);
    }
}
