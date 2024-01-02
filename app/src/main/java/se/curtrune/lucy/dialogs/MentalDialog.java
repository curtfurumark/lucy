package se.curtrune.lucy.dialogs;



import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;


public class MentalDialog extends BottomSheetDialogFragment {

    private SeekBar seekBarEnergy;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarStress;
    private SeekBar seekBarDepression;
    private TextView textViewDepression;
    private EditText editTextComment;
    private TextView textViewEnergy;
    private EditText editTextHeading;
    private TextView textViewItemID;
    private TextView textViewMentalID;
    private Mental mental;

    private String heading;
    private Button buttonSave;
    private int energyLevel;
    private int moodLevel;
    private int anxiety;
    private int stress;
    private long itemID = -1;
    public static boolean VERBOSE = false;
    //private boolean parentItem = false;

    public  enum Mode {
        CREATE, CREATE_WITH_ITEM, EDIT

    }
    private Mode mode = Mode.CREATE;
    public interface Callback{
        void onMental(Mental mental, Mode mode);
    }

    public MentalDialog(){
        mode = Mode.CREATE;
        log("MentalDialog default constructor");
        this.heading = "";
    }
    public MentalDialog(Item item ){
        log("MentalDialog(Item)");
        mode = Mode.CREATE_WITH_ITEM;
        this.itemID = item.getID();
        this.heading = item.getHeading();
    }
    public MentalDialog(Mental  mental){
        log("MentalDialog(Mental)");
        mode = Mode.EDIT;
        this.mental = mental;
        this.heading = mental.getHeading();
        this.itemID = mental.getItemID();
        this.energyLevel = mental.getEnergy();
        this.stress = mental.getStress();
        this.itemID = mental.getAnxiety();
        this.itemID = mental.getDepression();
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
        //setDefaults();
        initListeners();
        setUserInterface();
        return view;
    }

    private void initComponents(View view){
        log("...initComponents()");
        buttonSave = view.findViewById(R.id.mentalDialog_button);
        textViewEnergy = view.findViewById(R.id.moodEnergy_textEnergy);
        seekBarEnergy = view.findViewById(R.id.mentalDialog_energy);
        textViewDepression = view.findViewById(R.id.moodEnergy_textMood);
        seekBarDepression = view.findViewById(R.id.mentalDialog_mood);
        seekBarStress = view.findViewById(R.id.mentalDialog_stress);
        seekBarAnxiety = view.findViewById(R.id.mentalDialog_anxiety);
        editTextHeading = view.findViewById(R.id.mentalDialog_heading);
        editTextComment = view.findViewById(R.id.mentalDialog_comment);
        textViewMentalID = view.findViewById(R.id.mentalDialog_mentalID);
        textViewItemID = view.findViewById(R.id.mentalDialog_itemID);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            if( !mode.equals(Mode.EDIT)){
                mental = new Mental();
            }
            mental.setTitle(editTextHeading.getText().toString());
            mental.setComment(editTextComment.getText().toString());
            mental.setEnergy(energyLevel);
            mental.setDepression(moodLevel);
            mental.setItemID(itemID);
            mental.setAnxiety(anxiety);
            mental.setStress(stress);
            listener.onMental(mental, mode);

            dismiss();
        });
        seekBarAnxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                anxiety = seekBar.getProgress();
            }
        });
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int level, boolean fromUser) {
                //log("...onProgressChanged(...)", progress);
                energyLevel = level;
                textViewEnergy.setText(String.format("energy %d", energyLevel));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //log("...onStartTrackingTouch(SeekBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //log("...onStopTrackingTouch(SeekBar)");
            }
        });
        seekBarDepression.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int level, boolean fromUser) {
                moodLevel = level;
                textViewDepression.setText(String.format("mood %d", moodLevel));
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

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stress = seekBar.getProgress();
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Callback) context;
    }
    private void setUserInterface(){
        log("MentalDialog.setUserInterface()");
        if( mode.equals(Mode.CREATE_WITH_ITEM)) {
            editTextHeading.setText(heading);
        }
        if( mode.equals(Mode.CREATE)){
            editTextHeading.setHint("<heading here>");
        }
        editTextComment.setHint("<comment here>");
        if( mode.equals(Mode.EDIT)){
            editTextHeading.setText(mental.getHeading());
            editTextComment.setText(mental.getComment());
            seekBarEnergy.setProgress(mental.getEnergy());
            seekBarStress.setProgress(mental.getStress());
            seekBarDepression.setProgress(mental.getDepression());
            seekBarAnxiety.setProgress(mental.getAnxiety());
            String strItemID = String.format(Locale.ENGLISH, "item id: %d", mental.getItemID());
            textViewItemID.setText(strItemID);
            String strMentalID = String.format(Locale.ENGLISH, "mental id: %d", mental.getID());
            textViewMentalID.setText(strMentalID);

        }else {
            seekBarEnergy.setProgress(0);
            seekBarStress.setProgress(0);
            seekBarDepression.setProgress(0);
            seekBarAnxiety.setProgress(0);
        }
    }
}
