package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ListableAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.statistics.CategoryListable;
import se.curtrune.lucy.statistics.DateListable;
import se.curtrune.lucy.statistics.DurationStatistics;
import se.curtrune.lucy.util.Converter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DurationFragment extends Fragment implements ListableAdapter.Callback{



    private TextView textViewFirstDate;
    private TextView textViewLastDate;
    private TextView textViewTotalDuration;
    private ListableAdapter adapter;
    private RecyclerView recycler;
    private RadioButton buttonCategory;
    private RadioButton buttonDate;
    // TODO: Rename and change types of parameters

    private LocalDate firstDate;
    private LocalDate lastDate;
    private DurationStatistics statistics;
    private List<Listable> listables = new ArrayList<>();

    public DurationFragment() {
        log("DurationFragment()");
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DurationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DurationFragment newInstance() {
        return  new DurationFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("...onCreate(Bundle)");
        if (getArguments() != null) {
            log("...getArguments.onCreate(Bundle)");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        log("DurationFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.duration_fragment, container, false);
        initDefaults();
        initComponents(view);
        initRecycler();
        initListeners();
        setUserInterface();
        filter("vila");
        return view;
    }
    private void initComponents(View view){
        log("initComponents()");
        textViewFirstDate = view.findViewById(R.id.durationFragment_firstDate);
        textViewLastDate = view.findViewById(R.id.durationFragment_lastDate);
        recycler = view.findViewById(R.id.durationFragment_recycler);
        textViewTotalDuration = view.findViewById(R.id.durationFragment_totalDuration);
        buttonCategory = view.findViewById(R.id.durationFragment_category);
        buttonDate = view.findViewById(R.id.durationFragment_date);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewFirstDate.setOnClickListener(view->showDatePickerDialogFirstDate());
        textViewLastDate.setOnClickListener(view->showDatePickerDialogLastDate());
        buttonDate.setOnClickListener( view->showDate());
        buttonCategory.setOnClickListener(view->showCategory());
    }
    private void initDefaults(){
        log("...initDefaults()");
        lastDate = LocalDate.now();
        firstDate = lastDate.minusDays(6);
        statistics = new DurationStatistics(firstDate, lastDate, getContext());
    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new ListableAdapter(listables, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

    }
    @Override
    public void onItemClick(Listable item) {
        log("...onItemClick(Listable)");
        if( item instanceof DateListable){
            DateListable dateListable = (DateListable) item;
            adapter.setList(dateListable.getListableItems());
        }else if( item instanceof CategoryListable){
            CategoryListable categoryListable = (CategoryListable) item;
            adapter.setList(categoryListable.getListableItems());
        }else if( item instanceof Item){
            log("....you clicked an item");
            Toast.makeText(getContext(), "you clicked an item", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLongClick(Listable item) {
        log("...onLongClick(Listable)");
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        textViewFirstDate.setText(firstDate.toString());
        textViewLastDate.setText(lastDate.toString());
        buttonCategory.setChecked(true);
        textViewTotalDuration.setText(Converter.formatSecondsWithHours(statistics.getTotalDuration()));
        listables = statistics.getCategoryListables();
        adapter.setList(listables);
    }
    private void showCategory(){
        log("...showCategory()");
       //statistics.getCategoryListables().sort(Comparator.comparingLong(Listable::compare));
        List<Listable> list = statistics.getCategoryListables();
        list.sort(Comparator.comparingLong(Listable::compare));
        adapter.setList(list);
    }
    private void showDate(){
        log("...showDate()");
        adapter.setList(statistics.getDateListables());
    }

    private void showDatePickerDialogFirstDate() {
        log("...showDatePickerDialogFirstDate()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("...onDateSet(DatePicker, int year, int month, int dayOfMonth)");
            firstDate = LocalDate.of(year, month +1, dayOfMonth);
            updateStatistics();
        });
        datePickerDialog.show();
    }
    private void showDatePickerDialogLastDate() {
        log("...showDatePickerDialogLastDate()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("DatePickerDialog.onDateSet(year, month, dayOfMonth)");
                lastDate = LocalDate.of(year, month +1, dayOfMonth);
                updateStatistics();
            }
        });
        datePickerDialog.show();
    }
    private void filter(String str){
        log("...filter()");
        statistics.getItems().stream().filter(item -> item.contains(str)).forEach(System.out::println);
        long duration = statistics.getItems().stream().filter(item -> item.contains(str)).mapToLong(Item::getDuration).sum();
        log("duration vila", Converter.formatSecondsWithHours(duration));
    }
    private void updateStatistics(){
        log("...updateStatistics()");
        statistics = new DurationStatistics(firstDate, lastDate, getContext());
        setUserInterface();
        filter("vila");

    }
}