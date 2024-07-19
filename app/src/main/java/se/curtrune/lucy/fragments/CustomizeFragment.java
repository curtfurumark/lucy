package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.SimpleAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.dialogs.AddCategoryDialog;
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
   // private ListView listViewPanicUrls;
    private RecyclerView recyclerPanicUrls;
    private RecyclerView recyclerCategories;
    private RadioButton radioButtonGame;
    private RadioButton radioButtonWeb;
    private RadioButton radioButtonSequence;
    private RadioButton radioButtonICE;
    private RadioButton radioButtonSwedish;
    private RadioButton radioButtonEnglish;
    private RadioGroup radioGroupLanguage;
    private LinearLayout layoutPanicButton;
    private TextView textViewPanicButton;
    private TextView labelCategories;
    private List<String> categories;
    private SimpleAdapter categoryAdapter;
    private SimpleAdapter panicUrlAdapter;
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
        initPanicURLS();
        initRecyclerCategories();
        initListeners();
        setUserInterface();
        printSharedPreferences();
        return view;
    }
    private void addCategory(String category){
        log("...addCategory(String)", category);
        User.addCategory(category, getContext());
        categories.add(category);
        categoryAdapter.notifyDataSetChanged();
    }
    private void addPanicUrl(){
        log("...addPanicUrl()");
        String url = editTextUrl.getText().toString();
        if(!URLUtil.isValidUrl(url)){
            log("...not a valid url");
            Toast.makeText(getContext(), "not a valid url", Toast.LENGTH_LONG).show();
            return;
        }
        User.addPanicUrl(url, getContext());
        editTextUrl.setText("");
        initPanicURLS();
    }
    private void deletePanicURL(String url){
        log("...deletePanicURL()");
        User.deletePanicUrl(url, getContext());
        initPanicURLS();

    }
    private void initComponents(View view){
        log("...initComponents(View view)");
        editTextUrl = view.findViewById(R.id.customizeFragment_url);
        buttonAddUrl = view.findViewById(R.id.customizeFragment_buttonAddUrl);
        recyclerPanicUrls = view.findViewById(R.id.customizeFragment_panicUrls);
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
        recyclerCategories = view.findViewById(R.id.customizeFragment_recyclerCategories);
        labelCategories = view.findViewById(R.id.customizeFragment_labelCategory);
        radioButtonICE = view.findViewById(R.id.customizeFragment_radioButtonICE);
    }
    private void initListeners(){
        log("...initListeners()");
        labelCategories.setOnClickListener(view->showCategoryDialog());
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
        radioButtonICE.setOnClickListener(view->onClickICE());
        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            log("...onCheckChanged(RadioGroup, int)");
            setLanguage(checkedId);
        });
        textViewPanicButton.setOnClickListener(view->togglePanicButton());
    }
    private void initRecyclerCategories(){
        log("...initRecyclerCategories()");
        categories = new ArrayList<>(Arrays.asList( User.getCategories(getContext())));
        categoryAdapter = new SimpleAdapter(categories, category ->
                log("...onItemClick(String)", category));
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerCategories.setAdapter(categoryAdapter);
    }


    private void initPanicURLS(){
        log("...initPanicURLS())");
        Set<String> urls = User.getPanicUrls(getContext());
        for( String url: urls){
            log("...url", url);
        }
        panicUrlAdapter = new SimpleAdapter(new ArrayList<>(urls), url ->
                onPanicUrlClick(url));
        recyclerPanicUrls.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPanicUrls.setItemAnimator(new DefaultItemAnimator());
        recyclerPanicUrls.setAdapter(panicUrlAdapter);
    }
    private void onClickICE(){
        log("...onClickICE()");
    }

    private void onPanicUrlClick(String url) {
        log("...onPanicUrlClick()", url);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("delete" +  url);
        builder.setMessage("are you sure? ");
        builder.setPositiveButton("delete", (dialog, which) -> {
            log("...on positive button click");
            User.deletePanicUrl(url, getContext());
            panicUrlAdapter.setList(new ArrayList<>(User.getPanicUrls(getContext())));
        });
        builder.setNegativeButton("cancel", (dialog, which) -> {
            log("...on negative button click");
        });
        AlertDialog dialog = builder.create();
        dialog.show();

/*        Uri webPage = Uri.parse(url);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
            startActivity(intent);
        } catch (Exception e) {
            log("EXCEPTION, probably not valid ur");
            Toast.makeText(getContext(), "page not found", Toast.LENGTH_LONG).show();
        }*/
    }
    private void panicAction(Settings.PanicAction panicAction){
        log("...panicAction(PanicAction)", panicAction.toString());
/*        User.setPanicAction(panicAction, getContext());
        switch (panicAction){
            case URL:
            case GAME:
            case PENDING:
            case SEQUENCE:
            case ICE:
                showPanic
        }*/
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
    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        AddCategoryDialog dialog = new AddCategoryDialog();
        dialog.setListener(category -> {
            log("...onNewCategory(String)", category);
            addCategory(category);
        });
        dialog.show(getChildFragmentManager(), "add category");
    }
    private void showPanicAction(){


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