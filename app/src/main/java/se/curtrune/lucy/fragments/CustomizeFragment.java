package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Set;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomizeFragment extends Fragment {

    private CheckBox checkBoxPassword;
    private CheckBox checkBoxDarkMode;
    private EditText editTextUrl;
    private Button buttonAddUrl;
    private ListView listViewPanicUrls;
    private RadioButton radioButtonGame;
    private RadioButton radioButtonWeb;
    private RadioButton radioButtonSequence;
    private RadioButton radioButtonSwedish;
    private RadioButton radioButtonEnglish;
    private RadioGroup radioGroupLanguage;
    public CustomizeFragment() {
        // Required empty public constructor
    }


    public static CustomizeFragment newInstance() {
        return new CustomizeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments() != null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CustomizeFragment.onCreateView(...)");
         View view = inflater.inflate(R.layout.customize_fragment, container, false);
         initComponents(view);
         initListeners();
         initListView();
         setUserInterface();
         printSharedPreferences();
         return view;
    }
    private void addPanicUrl(){
        log("...addPanicUrl()");
        User.addPanicUrl(editTextUrl.getText().toString(), getContext());
        editTextUrl.setText("");
        initListView();
    }
    private void initComponents(View view){
        log("...initComponents(View view)");
        editTextUrl = view.findViewById(R.id.customizeFragment_url);
        buttonAddUrl = view.findViewById(R.id.customizeFragment_buttonAddUrl);
        listViewPanicUrls = view.findViewById(R.id.customizeFragment_panicUrls);
        checkBoxPassword = view.findViewById(R.id.customizeFragment_checkBoxPassword);
        checkBoxDarkMode = view.findViewById(R.id.customizeFragment_checkBoxDarkMode);
        radioButtonGame = view.findViewById(R.id.customizeFragment_radioButtonGame);
        radioButtonSequence = view.findViewById(R.id.customizeFragment_radioButtonSequence);
        radioButtonWeb = view.findViewById(R.id.customizeFragment_radioButtonWeb);
        radioButtonEnglish = view.findViewById(R.id.customizeFragment_radioButtonEnglish);
        radioButtonSwedish = view.findViewById(R.id.customizeFragment_radioButtonSwedish);
        radioGroupLanguage = view.findViewById(R.id.customizeFragment_radioGroupLanguage);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonAddUrl.setOnClickListener(view->addPanicUrl());
        checkBoxPassword.setOnClickListener(view->{
            if( checkBoxPassword.isChecked()){
                if( !User.usesPassword(getContext())){
                    showPasswordDialog();
                }
            }else{
                log("...remove password?");
            }
        });
        checkBoxDarkMode.setOnClickListener(view->toggleDarkMode());
        radioButtonGame.setOnClickListener(view->panicAction(Settings.PanicAction.GAME));
        radioButtonWeb.setOnClickListener(view->panicAction(Settings.PanicAction.URL));
        radioButtonSequence.setOnClickListener(view->panicAction(Settings.PanicAction.SEQUENCE));
        radioGroupLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setLanguage(checkedId);
            }
        });
    }
    private void initListView(){
        log("...initListView()");
        Set<String> urls = User.getPanicUrls(getContext());
        ArrayAdapter<String> arr = new ArrayAdapter<>(requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                urls.toArray(new String[1]));
        listViewPanicUrls.setAdapter(arr);
    }
    private void panicAction(Settings.PanicAction panicAction){
        log("...panicAction(PanicAction)", panicAction.toString());
        Toast.makeText(getContext(), panicAction.toString(), Toast.LENGTH_LONG).show();
        User.setPanicActions(panicAction, getContext());
    }
    private void printSharedPreferences(){
        Settings.printAll(requireContext());
    }
    private void setLanguage(int id){
        log("...setLanguage(int)", id);
        if( id == R.id.customizeFragment_radioButtonEnglish){
            log("ENGLISH");
            User.setLanguage("en", getContext());
        }else{
            log("SWEDISH");
            User.setLanguage("sv", getContext());
        }
    }
    private void setUserInterface(){
        log("...setUserInterface(()") ;
        checkBoxPassword.setChecked(User.usesPassword(getContext()));
        checkBoxDarkMode.setChecked(User.getDarkMode(getContext()));
        String language = User.getLanguage(getContext());
        switch (language){
            case "sv":
                radioButtonSwedish.setChecked(true);
                break;
            case "en":
                radioButtonEnglish.setChecked(true);
                break;
            default:
        }
    }
    private void showPasswordDialog(){
        log("...showPasswordDialog()");
        Toast.makeText(getContext(), "password dialog", Toast.LENGTH_LONG).show();
    }
    private void toggleDarkMode(){
        log("...toggleDarkMode()");
        Toast.makeText(getContext(), "light mode not implemented", Toast.LENGTH_LONG).show();
        //User.setUseDarkMode(checkBoxDarkMode.isChecked(), getContext());
        //SettingsWorker.toggleDarkMode(getContext());
    }
}