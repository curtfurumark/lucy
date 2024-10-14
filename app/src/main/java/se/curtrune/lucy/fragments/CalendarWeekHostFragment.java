package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import se.curtrune.lucy.R;
import se.curtrune.lucy.viewPagers.ViewPagerAdapter;


public class CalendarWeekHostFragment extends Fragment {

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    //private int numberFragments = 3;

    public CalendarWeekHostFragment() {
        // Required empty public constructor
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
        viewPagerAdapter = new ViewPagerAdapter(requireActivity());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
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
        log("...initViews(View)");
        viewPager = view.findViewById(R.id.calendarWeekHostFragment_viewPager);

    }
}