package se.curtrune.lucy.viewPagers;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.fragments.CalenderWeekFragment;


public class WeekViewPagerAdapter extends FragmentStateAdapter {
    private int prevPosition;
    private int numberFragments = 100;
    private List<Fragment> fragments = new ArrayList<>(100);
    private LocalDate date = LocalDate.now();
    public static boolean VERBOSE = false;
    public WeekViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        log("WeekViewPagerAdapter(FragmentActivity)");
        fragments = init();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        log("...createFragment(int)", position);
        if (fragments.get(position) == null) {
            fragments.set(position, new CalenderWeekFragment(getDate(position)));
        }
        return fragments.get(position);
    }

    private LocalDate getDate(int position){
        int numberOfWeeks = position - 50;
        return LocalDate.now().plusWeeks(numberOfWeeks);
    }

    @Override
    public int getItemCount() {
        if( VERBOSE) log("...getItemCount() fragments, size", fragments.size());
        return fragments.size();
    }
    private List<Fragment> init(){
        log("...init() number of fragments", numberFragments);
        for(int i = 0; i < numberFragments; i++){
            fragments.add(null);
        }
        return fragments;
    }
}
