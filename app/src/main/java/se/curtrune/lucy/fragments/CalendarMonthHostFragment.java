package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import se.curtrune.lucy.R;
import se.curtrune.lucy.viewPagers.MonthViewPagerAdapter;

public class CalendarMonthHostFragment extends Fragment {
    public static boolean VERBOSE = false;
    private MonthViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.calendar_month_host_fragment, container, false);
        log("CalendarMontHostFragment.onCreteView(...)");
        initViews(view);
        initViewPager();
        return view;
    }
    private void initViewPager(){
        log("...initViewPager()");
        viewPagerAdapter = new MonthViewPagerAdapter(requireActivity());
        viewPagerAdapter.setNumberFragments(24);
        viewPagerAdapter.setInitialPosition(12);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(12);
        viewPager.setOffscreenPageLimit(1);
        //viewPager.setOffscreenPageLimit(1);
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
        viewPager = view.findViewById(R.id.calendarMonthHostFragment_viewPager);
    }
}
