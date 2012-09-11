package by.fksis.schedule.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import by.fksis.schedule.API;
import by.fksis.schedule.R;
import by.fksis.schedule.adapters.WeekPagerAdapter;
import by.fksis.schedule.async.SynchronizationTask;
import com.WazaBe.HoloEverywhere.sherlock.SFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectView;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Aibolit.doInjections(this);

        getSupportActionBar().setTitle(R.string.schedule);
        API.loadCredentials(this);

        if (!API.credentialsPresent()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        viewPager.setAdapter(new WeekPagerAdapter(getSupportFragmentManager(), this));
        indicator.setViewPager(viewPager);
        indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        indicator.setTextColor(0xff888888);
        indicator.setTextSize(20);
        indicator.setSelectedColor(0xff444444);
        indicator.setSelectedBold(false);

        new SynchronizationTask(this, false).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        {
            MenuItem menuItem = menu.add(R.string.refresh);
            menuItem.setIcon(R.drawable.ic_reload);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    new SynchronizationTask(MainActivity.this, true).execute();
                    return true;
                }
            });
        }
        {
            MenuItem menuItem = menu.add(R.string.preferences);
            menuItem.setIcon(R.drawable.ic_settings);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
                    return true;
                }
            });
        }
        return true;
    }

    @InjectView(R.id.indicator)
    private TitlePageIndicator indicator;

    @InjectView(R.id.pager)
    private ViewPager viewPager;
}
