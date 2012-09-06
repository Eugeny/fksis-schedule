package by.fksis.schedule.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import by.fksis.schedule.R;
import by.fksis.schedule.app.DayScheduleFragment;

public class WeekPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(R.string.weekdays).split(" ")[position].toUpperCase();
    }

    public WeekPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return new DayScheduleFragment();
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

}
