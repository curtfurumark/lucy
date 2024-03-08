package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Period;


public class PeriodDialog extends BottomSheetDialogFragment {
    private EditText editTextDays;
    private Button buttonSave;
    private Chip chipMonday;
    private Chip chipTuesday;
    private Chip chipWednesday;
    private Chip chipThursday;
    private Chip chipFriday;
    private Chip chipSaturday;
    private Chip chipSunday;

    public interface Callback{
        void onPeriod(Period period);
    }

    private Callback listener;
    private enum Mode{
        CREATE, EDIT
    }
    public PeriodDialog(){
        log("AddItemFragment default constructor");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.period_dialog, container, false);

        initComponents(view);
        initListeners();

        return view;
    }
    private Period getPeriod(){
        log("...getPeriod()");
        Period period = new Period();
        period.setDays(Integer.parseInt(editTextDays.getText().toString()));
        return period;

    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonSave = view.findViewById(R.id.periodDialog_save);
        chipMonday = view.findViewById(R.id.periodDialog_monday);
        chipTuesday = view.findViewById(R.id.periodDialog_tuesday);
        chipWednesday = view.findViewById(R.id.periodDialog_wednesday);
        chipThursday = view.findViewById(R.id.periodDialog_thursday);
        chipFriday = view.findViewById(R.id.periodDialog_friday);
        chipSaturday = view.findViewById(R.id.periodDialog_saturday);
        chipSunday = view.findViewById(R.id.periodDialog_sunday);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            Period period = getPeriod();
            listener.onPeriod(period);
            dismiss();
        });

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Callback) context;
    }


}
