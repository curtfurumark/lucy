package se.curtrune.lucy.dialogs;


import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.app.Lucy;
import se.curtrune.lucy.workers.CategoryWorker;


public class MentalDialog extends BottomSheetDialogFragment {


    private SeekBar seekBarEnergy;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarStress;
    private SeekBar seekBarMood;

    private EditText editTextComment;

    private EditText editTextHeading;
    private TextView textViewItemID;
    private TextView textViewMentalID;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView labelEnergy;
    private TextView labelAnxiety;
    private TextView labelStress;
    private TextView labelMood;

    private Spinner spinnerCategory;
    private Mental mental;

    private String heading;
    private Button buttonSave;
    private ArrayAdapter<String> arrayAdapter;

    private LocalDate date;
    private LocalTime time;
    private Item item;
    private String[] categories;
    private int energy;
    private int mood;
    private int stress;
    private int anxiety;

    private long itemID = -1;
    public static boolean VERBOSE = true;
    private String category;

    public  enum Mode {
        CREATE, CREATE_WITH_ITEM, EDIT

    }
    private Mode mode;
    public interface Callback{
        void onMental(Mental mental, Mode mode);
    }

    public MentalDialog(){
        mode = Mode.CREATE;
        log("MentalDialog default constructor");
        this.heading = "";
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
    public MentalDialog(Item item ){
        this();
        log("MentalDialog(Item)");
        mode = Mode.CREATE_WITH_ITEM;
        this.item = item;
        this.itemID = item.getID();
        this.date = item.getDateUpdated();
        this.time = item.getTimeUpdated();
        this.heading = item.getHeading();
    }
    public MentalDialog(Mental  mental){
        this();
        log("MentalDialog(Mental)");
        if( VERBOSE) log(mental);
        mode = Mode.EDIT;
        this.mental = mental;
        this.heading = mental.getHeading();
        this.itemID = mental.getItemID();

        category = mental.getCategory();
    }

    private Callback listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if( VERBOSE) log("MentalDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.mental_dialog, container, false);
        if( savedInstanceState != null) {
            heading = savedInstanceState.getString("heading");
        }else{
            log("...savedInstance is null");
        }
        initComponents(view );
        initDefaults();
        initListeners();
        initSpinnerCategory();
        setUserInterface();
        return view;
    }
    private Mental getMental(){
        log("...getMental()");
        if( !mode.equals(Mode.EDIT)){
            mental = new Mental();
        }
        mental.setHeading(editTextHeading.getText().toString());
        mental.setComment(editTextComment.getText().toString());
        mental.setMood(seekBarMood.getProgress());
        mental.setDate(date);
        mental.setTime(time);
        mental.setItemID(itemID);
        mental.setCategory(category);
        mental.setAnxiety(seekBarAnxiety.getProgress() - Constants.ANXIETY_OFFSET);
        mental.setStress(seekBarStress.getProgress() - Constants.STRESS_OFFSET);
        mental.setMood(seekBarMood.getProgress() - Constants.MOOD_OFFSET);
        mental.setEnergy(seekBarEnergy.getProgress() - Constants.ENERGY_OFFSET);
        return mental;
    }

    private void initComponents(View view){
        log("...initComponents()");
        buttonSave = view.findViewById(R.id.mentalDialog_button);
        seekBarEnergy = view.findViewById(R.id.mentalDialog_energy);
        seekBarMood = view.findViewById(R.id.mentalDialog_mood);
        seekBarStress = view.findViewById(R.id.mentalDialog_stress);
        seekBarAnxiety = view.findViewById(R.id.mentalDialog_anxiety);
        editTextHeading = view.findViewById(R.id.mentalDialog_heading);
        editTextComment = view.findViewById(R.id.mentalDialog_comment);
        textViewMentalID = view.findViewById(R.id.mentalDialog_mentalID);
        textViewItemID = view.findViewById(R.id.mentalDialog_itemID);
        spinnerCategory = view.findViewById(R.id.mentalDialog_spinnerCategories);
        textViewDate = view.findViewById(R.id.mentalDialog_date);
        textViewTime = view.findViewById(R.id.mentalDialog_time);
        labelEnergy = view.findViewById(R.id.mentalDialog_labelEnergy);
        labelAnxiety = view.findViewById(R.id.mentalDialog_labelAnxiety);
        labelStress = view.findViewById(R.id.mentalDialog_labelStress);
        labelMood = view.findViewById(R.id.mentalDialog_labelMood);
    }
    private void initDefaults(){
        log("...initDefaults()");
        categories = CategoryWorker.getCategories(getContext());
        assert categories.length > 0;
        category = categories[0];

    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            mental = getMental();
            listener.onMental(mental, mode);
            dismiss();
        });
        textViewDate.setOnClickListener(view->showDateDialog());
        textViewTime.setOnClickListener(view->showTimeDialog());
        seekBarAnxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                anxiety = progress - Constants.ANXIETY_OFFSET;
                setMentalLabels();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int level, boolean fromUser) {
                energy = level - Constants.ENERGY_OFFSET;
                setMentalLabels();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //log("...onStartTrackingTouch(SeekBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int level, boolean fromUser) {
                mood = level - Constants.MOOD_OFFSET;
                setMentalLabels();
                //textViewDepression.setText(String.format(Locale.ENGLISH, "mood %d", seekBarMood.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarStress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stress = progress - Constants.STRESS_OFFSET;
                setMentalLabels();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void initSpinnerCategory(){
        log("...initSpinnerCategory()");
        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategory.setAdapter(arrayAdapter);
        int pos = arrayAdapter.getPosition("PENDING");
        log("...pos of PENDING", pos);
        spinnerCategory.setSelection(arrayAdapter.getPosition(category));
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //ItemEditor.this.type = Type.values()[position];
                category = (String) spinnerCategory.getSelectedItem();
                log("...category chosen", category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinnerCategory.setSelection(arrayAdapter.getPosition(category));
    }

    @Override

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
/*        try {
            this.listener = (Callback) context;
        }catch (ClassCastException exception){
            log("...class cast exception");
            exception.printStackTrace();
        }*/
    }
    public void setCallback(MentalDialog.Callback callback){
        this.listener = callback;
    }
    private void setMentalLabels(){
        log("...setMentalLabels()");
        String strEnergy = String.format("energy %d", energy);
        labelEnergy.setText(strEnergy);

        String strMood = String.format("mood %d", mood);
        labelMood.setText(strMood);

        String strAnxiety = String.format("anxiety %d", anxiety);
        labelAnxiety.setText(strAnxiety);
        String strStress = String.format("stress %d", stress);
        labelStress.setText(strStress);
    }
    private void setSeekBars(){
        log("...setSeekBars()");
        seekBarEnergy.setProgress(energy + Constants.ENERGY_OFFSET);
        seekBarStress.setProgress(stress + Constants.STRESS_OFFSET);
        seekBarMood.setProgress(mood + Constants.MOOD_OFFSET);
        seekBarAnxiety.setProgress(anxiety + Constants.ANXIETY_OFFSET);
    }
    private void setUserInterface(){
        log("...setUserInterface()", mode.toString());
        if( mode.equals(Mode.CREATE_WITH_ITEM)) {
            editTextHeading.setText(heading);
            textViewTime.setText(Converter.format(time));
            textViewDate.setText(date.toString());
            buttonSave.setText("save");
        }
        if( mode.equals(Mode.CREATE)){
            textViewTime.setText(Converter.format(time));
            textViewDate.setText(date.toString());
            buttonSave.setText("save");
        }
        if( mode.equals(Mode.EDIT)){
            log(mental);
            editTextHeading.setText(mental.getHeading());
            editTextComment.setText(mental.getComment());
            buttonSave.setText("update");
            time = mental.getTime();
            textViewTime.setText(time.toString());
            date = mental.getDate();
            textViewDate.setText(date.toString());
            seekBarEnergy.setProgress(mental.getEnergy() + Constants.ENERGY_OFFSET);
            seekBarStress.setProgress(mental.getStress() + Constants.STRESS_OFFSET);
            seekBarMood.setProgress(mental.getMood() + Constants.MOOD_OFFSET);
            setSpinnerCategory(mental.getCategory());
            seekBarAnxiety.setProgress(mental.getAnxiety() + Constants.ANXIETY_OFFSET);
            String strItemID = String.format(Locale.ENGLISH, "item id: %d", mental.getItemID());
            textViewItemID.setText(strItemID);
            String strMentalID = String.format(Locale.ENGLISH, "mental id: %d", mental.getID());
            textViewMentalID.setText(strMentalID);
        }else {
            energy = 0;
            anxiety = 0;
            stress = 0;
            mood = 0;
            setMentalLabels();
            setSeekBars();

        }
        updateUserInterface();
    }
    private void setSpinnerCategory(String category){
        log("...setSpinnerCategory(String) ", category);
        spinnerCategory.setSelection(arrayAdapter.getPosition(category));
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            date = LocalDate.of(year, month +1, dayOfMonth);
            textViewDate.setText(date.toString());
        });
        datePickerDialog.show();
    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        int minutes = time.getMinute();
        int hour = time.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                time = LocalTime.of(hourOfDay, minute);
                textViewTime.setText(time.toString());
                }, hour, minutes, true);
            timePicker.show();
    }

    /**
     * sets labels to current progress
     */
    private void updateUserInterface(){
        log("...updateUserInterface()");
        setMentalLabels();
/*        String strEnergy = String.format("energy %d", seekBarEnergy.getProgress() - Lucy.ENERGY_OFFSET);
        String strMood = String.format("mood %d", seekBarMood.getProgress() - Lucy.MOOD_OFFSET);
        String strAnxiety = String.format("anxiety %d", seekBarAnxiety.getProgress());
        String strStress = String.format("stress %d", seekBarStress.getProgress());
        labelEnergy.setText(strEnergy);
        labelMood.setText(strMood);
        labelStress.setText(strStress);
        labelAnxiety.setText(strAnxiety);*/

    }
}
