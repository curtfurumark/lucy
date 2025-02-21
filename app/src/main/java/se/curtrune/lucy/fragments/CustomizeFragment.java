package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.SimpleAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.UserPrefs;
import se.curtrune.lucy.dialogs.AddCategoryDialog;
import se.curtrune.lucy.dialogs.AddPanicURLDialog;
import se.curtrune.lucy.dialogs.PasswordDialog;
import se.curtrune.lucy.workers.SettingsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomizeFragment extends Fragment {

    private CheckBox checkBoxPassword;
    private CheckBox checkBoxDarkMode;
    //private EditText editTextUrl;
    private EditText editTextICE;
    private TextView textViewAddURL;
    private TextView textViewAddICE;
    private TextView textViewAddCategory;

    private RecyclerView recyclerPanicUrls;
    private RecyclerView recyclerCategories;
    private Spinner spinnerFirstPage;
    private RadioButton radioButtonGame;
    private RadioButton radioButtonWeb;
    private RadioButton radioButtonSequence;
    private RadioButton radioButtonPending;
    private RadioButton radioButtonICE;
    private RadioButton radioButtonSwedish;
    private RadioButton radioButtonEnglish;
    private RadioGroup radioGroupLanguage;
    private LinearLayout layoutPanicButton;
    private LinearLayout layoutCategories;
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
        initSpinnerFirstPage();
        initListeners();
        setUserInterface();
        printSharedPreferences();
        return view;
    }
    private void addCategory(String category){
        log("...addCategory(String)", category);
        UserPrefs.addCategory(category, getContext());
        categories.add(category);
        categoryAdapter.notifyDataSetChanged();
    }
    private void addICE() {
        log("...addICE");
        String stringICE = editTextICE.getText().toString();
        if (stringICE.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.missing_telephone_number), Toast.LENGTH_LONG).show();
            return;
        }
        int intPhoneNumber;
        try {
            intPhoneNumber = Integer.parseInt(stringICE);
        } catch (NumberFormatException exception) {
            log("EXCEPTION, parsing ice");
            exception.printStackTrace();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        UserPrefs.setIcePhoneNumber(intPhoneNumber, getContext());
        Toast.makeText(getContext() ,getString( R.string.ice_added), Toast.LENGTH_LONG).show();

    }
    private void addPanicUrl(String url){
        log("...addPanicUrl()");
        if(!URLUtil.isValidUrl(url)){
            log("...not a valid url");
            Toast.makeText(getContext(), "not a valid url", Toast.LENGTH_LONG).show();
            return;
        }
        UserPrefs.addPanicUrl(url, getContext());
        initPanicURLS();
    }
    private  void deleteCategory(String category){
        log("...deleteCategory(String)", category);
        UserPrefs.deleteCategory(category, getContext());

    }
    private void deletePanicURL(String url){
        log("...deletePanicURL()");
        UserPrefs.deletePanicUrl(url, getContext());
        initPanicURLS();
    }

    private void initComponents(View view){
        log("...initComponents(View view)");
        textViewAddURL = view.findViewById(R.id.customizeFragment_addURL);
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
        editTextICE = view.findViewById(R.id.customizeFragment_editTextICE);
        textViewAddICE = view.findViewById(R.id.customizeFragment_addICE);
        radioButtonPending = view.findViewById(R.id.customizeFragment_radioButtonPending);
        labelCategories= view.findViewById(R.id.customizeFragment_labelCategory);
        textViewAddCategory = view.findViewById(R.id.customizeFragment_addCategory);
        layoutCategories = view.findViewById(R.id.customizeFragment_layoutCategories);
        spinnerFirstPage = view.findViewById(R.id.customizeFragment_spinnerSplashActivity);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewAddCategory.setOnClickListener(view->showCategoryDialog());
        labelCategories.setOnClickListener(view->toggleCategories());
        textViewAddURL.setOnClickListener(view->showAddUrlDialog());
        checkBoxPassword.setOnClickListener(view->{
            if( checkBoxPassword.isChecked()){
                if( !UserPrefs.usesPassword(getContext())){
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
        radioButtonICE.setOnClickListener(view->panicAction(Settings.PanicAction.ICE));
        radioButtonPending.setOnClickListener(view->panicAction(Settings.PanicAction.PENDING));
        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            log("...onCheckChanged(RadioGroup, int)");
            setLanguage(checkedId);
        });
        textViewPanicButton.setOnClickListener(view->togglePanicButton());
        textViewAddICE.setOnClickListener(view->addICE());
    }
    private void initRecyclerCategories(){
        if( VERBOSE) log("...initRecyclerCategories()");
        categories = new ArrayList<>(Arrays.asList( UserPrefs.getCategories(getContext())));
        categoryAdapter = new SimpleAdapter(categories, category ->{
            log("...onItemClick(String)", category);
            showDeleteCategoryDialog();
        });
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerCategories.setAdapter(categoryAdapter);
    }


    private void initPanicURLS(){
        log("...initPanicURLS())");
        Set<String> urls = UserPrefs.getPanicUrls(getContext());
        for( String url: urls){
            log("...url", url);
        }
        panicUrlAdapter = new SimpleAdapter(new ArrayList<>(urls), url ->
                onPanicUrlClick(url));
        recyclerPanicUrls.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPanicUrls.setItemAnimator(new DefaultItemAnimator());
        recyclerPanicUrls.setAdapter(panicUrlAdapter);
    }
    private void initSpinnerFirstPage(){
        log("...initSpinnerFirstPage()");
        ArrayAdapter<Settings.StartActivity> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,Settings.StartActivity.values() );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerFirstPage.setAdapter(adapter);
        //spinnerFirstPage.setSelection(type.ordinal());
        spinnerFirstPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("spinner first page.onItemSelected(....)", adapter.getItem(position).toString());
                UserPrefs.setFirstPage(adapter.getItem(position), getContext());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void onPanicUrlClick(String url) {
        log("...onPanicUrlClick()", url);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String deleteTitle = String.format(Locale.getDefault(), "%s %s?", getString(R.string.delete), url);
        builder.setTitle(deleteTitle);
        builder.setMessage(getString(R.string.are_you_sure));
        builder.setPositiveButton(getString(R.string.delete), (dialog, which) -> {
            log("...on positive button click");
            UserPrefs.deletePanicUrl(url, getContext());
            panicUrlAdapter.setList(new ArrayList<>(UserPrefs.getPanicUrls(getContext())));
        });
        builder.setNegativeButton(getString(R.string.dismiss), (dialog, which) -> {
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
            Toast.makeText(getContext(), "page not found", Toast.LENGTH_LONG).setMentalType();
        }*/
    }
    private void panicAction(Settings.PanicAction panicAction){
        log("...panicAction(PanicAction)", panicAction.toString());
        if( panicAction.equals(Settings.PanicAction.ICE)){
            if( UserPrefs.getICE(getContext() )== -1){
                Toast.makeText(getContext(), "please add an ice phone number", Toast.LENGTH_LONG).show();
                return;
            }
        }
        UserPrefs.setPanicAction(panicAction, getContext());
    }
    private void printSharedPreferences(){
        Settings.printAll(requireContext());
    }
    private void removePassword(){
        log("...removePassword()");
        //TODO, confirm with pwd
        Toast.makeText(getContext(), "remove password, are you sure?", Toast.LENGTH_LONG).show();
        UserPrefs.setUsesPassword(false, getContext());
    }
    private void setLanguage(int id){
        log("...setLanguage(int)", id);
        if( id == R.id.customizeFragment_radioButtonEnglish){
            log("ENGLISH");
            UserPrefs.setLanguage("en", getContext());
        }else{
            log("SWEDISH");
            UserPrefs.setLanguage("sv", getContext());
        }
    }
    private void setUserInterface(){
        log("...setUserInterface()") ;
        checkBoxPassword.setChecked(UserPrefs.usesPassword(getContext()));
        checkBoxDarkMode.setChecked(UserPrefs.getDarkMode(getContext()));
        setLanguage();
        setPanicAction();
    }
    private void setLanguage(){
        log("..setLanguage()");
        String language = UserPrefs.getLanguage(getContext());
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
        Settings.PanicAction panicAction = UserPrefs.getPanicAction(getContext());
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
            case ICE:
                radioButtonICE.setChecked(true);
                break;
            case PENDING:
                radioButtonPending.setChecked(true);
                break;
        }
        if( UserPrefs.getICE(getContext()) != -1){
            String stringICE = String.valueOf(UserPrefs.getICE(getContext()));
            editTextICE.setText(stringICE);
        }
    }
    private void showAddUrlDialog(){
        log("...showAddUrlDialog()");
        AddPanicURLDialog dialog =  new AddPanicURLDialog();
        dialog.setListener(new AddPanicURLDialog.Callback() {
            @Override
            public void onNewPanicURL(String url) {
                log("...onNewPanicURL(String)", url);
                addPanicUrl(url);
            }
        });
        dialog.show(getChildFragmentManager(), "add url");
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
    private void showDeleteCategoryDialog(){
        log("...showDeleteCategoryDialog()");

    }
    private void showPasswordDialog(){
        log("...showPasswordDialog()");
        PasswordDialog dialog = new PasswordDialog();
        dialog.setCallback(pwd -> {
            log("...onPassword(String)", pwd);
            UserPrefs.setUsesPassword(true, getContext());
            UserPrefs.setPassword(pwd, getContext());
        });
        dialog.show(getChildFragmentManager(), "set password");
    }
    private void toggleCategories(){
        log("...toggleCategories()");
        if( layoutCategories.getVisibility() == View.VISIBLE){
            layoutCategories.setVisibility(View.GONE);
        }else{
            layoutCategories.setVisibility(View.VISIBLE);
        }

    }
    private void toggleDarkMode(){
        log("...toggleDarkMode()");
        UserPrefs.setUseDarkMode(checkBoxDarkMode.isChecked(), getContext());
        if(checkBoxDarkMode.isChecked()){
            SettingsWorker.setDarkMode();
        }else{
            SettingsWorker.setLightMode();
        }
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