package se.curtrune.lucy.screens.week_calendar;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import se.curtrune.lucy.R;
import se.curtrune.lucy.viewPagers.WeekViewPagerAdapter;


public class CalendarWeekHostFragment extends Fragment {

    private ViewPager2 viewPager;
    private WeekViewPagerAdapter viewPagerAdapter;
    //private int numberFragments = 3;
    public static boolean VERBOSE = false;

    public CalendarWeekHostFragment() {
        log("CalendarWeekHostFragment()");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.calendar_week_host_fragment, container, false);
        log("CalendarWeekHostFragment.onCreteView(...)");
        initViews(view);
        initViewPager();
        return view;
    }
    private void initViewPager(){
        log("...initViewPager()");
        viewPagerAdapter = new WeekViewPagerAdapter(requireActivity());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(50);
        viewPager.setOffscreenPageLimit(1);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                log("...onPageSelected(int)", position);
            }
        });
    }
    private void initViews(View view){
        if( VERBOSE) log("...initViews(View)");
        viewPager = view.findViewById(R.id.calendarWeekHostFragment_viewPager);
    }
}