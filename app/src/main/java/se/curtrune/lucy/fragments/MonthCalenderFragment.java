package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.adapters.MonthAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthCalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthCalenderFragment extends Fragment {
    private TextView monthYearText;
    private Button buttonNext;
    private Button buttonPrev;
    private RecyclerView recyclerCalender;
    private RecyclerView recyclerDay;

    private LocalDate selectedDate;
    private MonthAdapter monthDayAdapter;
    private ItemAdapter itemAdapter;
    private List<CalenderDate> calenderDates;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthCalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthCalenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthCalenderFragment newInstance(String param1, String param2) {
        MonthCalenderFragment fragment = new MonthCalenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.my_calender_activity, container, false);
        initComponents(view);
        selectedDate = LocalDate.now();
        calenderDates = getCalenderDates(selectedDate);
        initRecycler(calenderDates);
        initListeners();
        setMonthYearLabel();
        return view;
    }
    private List<CalenderDate> getCalenderDates(LocalDate date){
        log("...getCalenderDate(LocalDate)", date.toString());
        List<CalenderDate> calenderDates = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        log("...yearMonth", yearMonth.toString());
        LocalDate firstDateOfMonth =  date.withDayOfMonth( 1);
        log("...firstDateOfMonth", firstDateOfMonth.toString());
        int daysInMonth = yearMonth.lengthOfMonth();
        log("...daysInMonth", daysInMonth);
        List<Item> itemsMonth = ItemsWorker.selectCalenderItems(yearMonth, getContext());
        log("...number of events this month", itemsMonth.size());
        //itemsMonth.forEach(System.out::println);
        LocalDate currentDate = firstDateOfMonth;
        int numberDays = daysInMonth;
        int firstDate = firstDateOfMonth.getDayOfWeek().getValue();
        int offset = firstDate;
        currentDate = currentDate.minusDays(offset - 1);
        log("firstDate", firstDate);
        for( int i = 1; i <= 42; i++){
            CalenderDate calenderDate = new CalenderDate();
            calenderDate.setDate(currentDate);
            //calenderDate.setItems(new ArrayList<>());
            LocalDate finalCurrentDate = currentDate;
            calenderDate.setItems(itemsMonth.stream().filter(item -> item.isDate(finalCurrentDate)).collect(Collectors.toList()));
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }

/*        for(int i = 0; i < numberDays; i++ ){
            CalenderDate calenderDate = new CalenderDate();
            //log("...currentDate", currentDate.toString());
            calenderDate.setDate(currentDate);
            LocalDate finalCurrentDate = currentDate;
            calenderDate.setItems(itemsMonth.stream().filter(item -> item.isDate(finalCurrentDate)).collect(Collectors.toList()));
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }*/
        return calenderDates;
    }
    private List<String> daysInMonthArray(LocalDate date){
        log("...daysInMonthArray(LocalDate)", date.toString());
        List<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for( int i = 1; i <= 42; i++){
            if( i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }

        }
        return daysInMonthArray;
    }
    private void initComponents(View view){
        recyclerCalender = view.findViewById(R.id.myCalender_recycler);
        monthYearText = view.findViewById(R.id.myCalender_month);
        buttonPrev = view.findViewById(R.id.monthCalender_buttonPrev);
        buttonNext = view.findViewById(R.id.monthCalender_buttonNexgt);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonNext.setOnClickListener(view->nextMonthAction());
        buttonPrev.setOnClickListener(view->previousMonthAction());
    }
    private void initRecycler(List<CalenderDate> calenderDates){
        log("...initRecycler()");
        monthDayAdapter = new MonthAdapter(calenderDates, calenderDate -> {
            log("onDateClick(CalenderDate)", calenderDate.getDate().toString());
            Toast.makeText(getContext(), calenderDate.toString(), Toast.LENGTH_LONG).show();
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerCalender.setLayoutManager(layoutManager);
        recyclerCalender.setItemAnimator(new DefaultItemAnimator());
        recyclerCalender.setAdapter(monthDayAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerCalender);
    }
    private void initRecyclerDay(){
        log("...initRecyclerDay()");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerDay.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(null, new ItemAdapter.Callback() {
            @Override
            public void onItemClick(Item item) {
                log("...onItemClick(Item)", item.getHeading());
            }

            @Override
            public void onLongClick(Item item) {

            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {

            }
        });
        recyclerDay.setAdapter(itemAdapter);

    }
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);

    }
    public void nextMonthAction(){
        log("...nextMonthAction()");
        selectedDate = selectedDate.plusMonths(1);
        monthDayAdapter.setList(getCalenderDates(selectedDate));
        setMonthYearLabel();

    }
    public void previousMonthAction(){
        log("...previousMonthAction()");
        selectedDate = selectedDate.minusMonths(1);
        monthDayAdapter.setList(getCalenderDates(selectedDate));
        setMonthYearLabel();
    }
    private void setMonthYearLabel(){
        monthYearText.setText(monthYearFromDate(selectedDate));
    }
}