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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Set;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.dialogs.PasswordDialog;

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
    private LinearLayout layoutPanicButton;
    private TextView textViewPanicButton;
    public static boolean VERBOSE = true;
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
        initListView();
        initListeners();
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
        layoutPanicButton = view.findViewById(R.id.customizeFragment_layoutPanicButton);
        textViewPanicButton = view.findViewById(R.id.customizeFragment_labelPanicButton);
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
                removePassword();
            }
        });
        checkBoxDarkMode.setOnClickListener(view->toggleDarkMode());
        radioButtonGame.setOnClickListener(view->panicAction(Settings.PanicAction.GAME));
        radioButtonWeb.setOnClickListener(view->panicAction(Settings.PanicAction.URL));
        radioButtonSequence.setOnClickListener(view->panicAction(Settings.PanicAction.SEQUENCE));
        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            log("...onCheckChanged(RadioGroup, int)");
            setLanguage(checkedId);
        });
        listViewPanicUrls.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            log("...onItemClick(AdapterView<?>, View, int, long)");
            String url = (String) listViewPanicUrls.getAdapter().getItem(position);
            log("...url", url);
        });
        textViewPanicButton.setOnClickListener(view->togglePanicButton());
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
        User.setPanicAction(panicAction, getContext());
    }
    private void printSharedPreferences(){
        Settings.printAll(requireContext());
    }
    private void removePassword(){
        log("...removePassword()");
        //TODO, confirm with pwd
        Toast.makeText(getContext(), "remove password, are you sure?", Toast.LENGTH_LONG).show();
        User.setUsesPassword(false, getContext());
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
        log("...setUserInterface()") ;
        checkBoxPassword.setChecked(User.usesPassword(getContext()));
        checkBoxDarkMode.setChecked(User.getDarkMode(getContext()));
        setLanguage();
        setPanicAction();
    }
    private void setLanguage(){
        log("..setLanguage()");
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
    private void setPanicAction(){
        log("...setPanicAction()");
        Settings.PanicAction panicAction = User.getPanicAction(getContext());
        log("...panicAction", panicAction.toString());
        switch (panicAction){
            case URL:
                radioButtonWeb.setChecked(true);
                break;
            case GAME:
                radioButtonGame.setChecked(true);
                break;
            case SEQUENCE:
                radioButtonSequence.setChecked(true);
                break;
        }

    }
    private void showPasswordDialog(){
        log("...showPasswordDialog()");
        PasswordDialog dialog = new PasswordDialog();
        dialog.setCallback(pwd -> {
            log("...onPassword(String)", pwd);
            User.setUsesPassword(true, getContext());
            User.setPassword(pwd, getContext());
        });
        dialog.show(getChildFragmentManager(), "set password");
    }
    private void toggleDarkMode(){
        log("...toggleDarkMode()");
        Toast.makeText(getContext(), "light mode not implemented", Toast.LENGTH_LONG).show();
        //User.setUseDarkMode(checkBoxDarkMode.isChecked(), getContext());
        //SettingsWorker.toggleDarkMode(getContext());
    }
    private void togglePanicButton(){
        log("...togglePanicButton()");
        if( layoutPanicButton.getVisibility() == View.GONE){
            layoutPanicButton.setVisibility(View.VISIBLE);
        }else{
            layoutPanicButton.setVisibility(View.GONE);
        }

    }
}