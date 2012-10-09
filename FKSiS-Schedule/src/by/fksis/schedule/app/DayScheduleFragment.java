package by.fksis.schedule.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.fksis.schedule.Preferences;
import by.fksis.schedule.R;
import by.fksis.schedule.Util;
import by.fksis.schedule.dal.Broadcast;
import by.fksis.schedule.dal.Database;
import by.fksis.schedule.dal.ScheduleClass;
import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectView;
import com.ormy.Application;
import com.ormy.DatabaseObserver;
import com.ormy.Model;

import java.util.Calendar;
import java.util.List;

public class DayScheduleFragment extends Fragment implements DatabaseObserver {
    private Calendar date;
    private int weekNumber, dayOfWeek;

    public DayScheduleFragment() {
    }

    public DayScheduleFragment(Calendar date) {
        this.date = date;
        dayOfWeek = Util.getDayOfWeekIndex(date);
        weekNumber = Util.getScheduleWeek(date.getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        Application.getDatabase().registerObserver(this);
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        Application.getDatabase().unregisterObserver(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_day_schedule, c, false);
        Aibolit.doInjections(this, view);
        return view;
    }

    public void refresh() {
        broadcastContainer.removeAllViews();

        List<Broadcast> broadcasts = Broadcast.get(Broadcast.class)
                .filter("groupList%", "%" + new Preferences(getActivity()).getGroup() + "%")
                .list();

        for (Broadcast broadcast : broadcasts)
            if (!broadcast.isExpired()) {
                View broadcastView = LayoutInflater.from(getActivity()).inflate(R.layout.day_broadcast_line, broadcastContainer, false);
                TextView text = (TextView) broadcastView;
                text.setText(broadcast.text);
                broadcastContainer.addView(broadcastView);
            }


        container.removeAllViews();

        List<ScheduleClass> classes = ScheduleClass.get(ScheduleClass.class)
                .filter("weeks%", "%" + weekNumber + "%")
                .filter("day", dayOfWeek)
                .filter("studentGroup", new Preferences(getActivity()).getGroup())
                .filter("subgroups%", "%" + new Preferences(getActivity()).getSubgroupString() + "%")
                .list();
        for (ScheduleClass clazz : classes) {
            View lineView = LayoutInflater.from(getActivity()).inflate(R.layout.day_schedule_line, container, false);
            LineViewHolder holder = new LineViewHolder();
            Aibolit.doInjections(holder, lineView);

            holder.name.setText(clazz.name);
            String subgroupText = "";
            if (clazz.subgroups.length() != 2)
                subgroupText = ", " + getActivity().getString(R.string.subgroup_text) + " " + clazz.subgroups;
            holder.teacher.setText(Util.defaultValue(clazz.type, "") + " " + Util.defaultValue(clazz.teacher, ""));
            holder.time.setText(getResources().getStringArray(R.array.timeSlotStart)[clazz.timeSlot]);
            holder.time2.setText(getResources().getStringArray(R.array.timeSlotEnd)[clazz.timeSlot]);
            holder.room.setText(clazz.room);
            container.addView(lineView);
        }
    }

    // ViewPager onPause() fix
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    @InjectView(R.id.container)
    private LinearLayout container;

    @InjectView(R.id.broadcast_container)
    private LinearLayout broadcastContainer;

    @Override
    public void databaseObjectUpdated(Model<?> model) {
        if (Database.autoRefresh)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
    }

    private class LineViewHolder {
        @InjectView(R.id.name)
        public TextView name;

        @InjectView(R.id.time)
        public TextView time;

        @InjectView(R.id.time2)
        public TextView time2;

        @InjectView(R.id.teacher)
        public TextView teacher;

        @InjectView(R.id.room)
        public TextView room;
    }
}
