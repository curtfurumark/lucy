package se.curtrune.lucy.viewPagers;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.LocalDate;

import se.curtrune.lucy.fragments.CalenderWeekFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {
    private int prevPosition;
    private int numberFragments = 3;
    private LocalDate date = LocalDate.now();
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        log("...createFragment(int)", position);
        Fragment fragment;
        fragment = new CalenderWeekFragment(getDate(position));
        prevPosition = position;
        return fragment;
    }

    private LocalDate getDate(int position){
        int numberOfWeeks = position - 1;
        return LocalDate.now().plusWeeks(numberOfWeeks);

    }
    @Override
    public int getItemCount() {
        return numberFragments;
    }
}
