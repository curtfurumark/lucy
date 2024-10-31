package se.curtrune.lucy.viewPagers;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.fragments.CalenderMonthFragment;
import se.curtrune.lucy.fragments.DummyMonthFragment;

public class MonthViewPagerAdapter extends FragmentStateAdapter {
    private int nFragments = 24;
    private int initialPosition;
    private YearMonth currentYearMonth = YearMonth.now();
    //private List<Fragment> fragments = new ArrayList<>(100);
    public MonthViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        log("MonthViewPagerAdapter(FragmentActivity)");
        //fragments = initFragments();
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        log("...createFragment(int)", position);
        //return new DummyMonthFragment(getYearMonth(position), position);
        return new CalenderMonthFragment(getYearMonth(position));
/*        if( fragments.get(position) == null){
            //fragments.set(position, new CalenderMonthFragment(getYearMonth(position)));
            YearMonth yearMonth = getYearMonth(position);
            log(yearMonth.toString());
            fragments.set(position, new DummyMonthFragment(getYearMonth(position), position));
        }
        return fragments.get(position);*/
    }

    @Override
    public int getItemCount() {
        return nFragments;
    }
    private YearMonth getYearMonth(int position){
        int nMonths = position - initialPosition;
        return currentYearMonth.plusMonths(nMonths);

    }
    private List<Fragment> initFragments(){
        log("...initFragments()>");
        List<Fragment> fragments1 = new ArrayList<>(100);
        for(int i = 0; i < nFragments; i++){
            fragments1.add(null);
        }
        return fragments1;
    }
    public void setInitialPosition(int initialPosition){
        this.initialPosition = initialPosition;
    }

    public void setNumberFragments(int nFragments) {
        log("...setNumberFragments(int)", nFragments);
        this.nFragments = nFragments;
    }
}
