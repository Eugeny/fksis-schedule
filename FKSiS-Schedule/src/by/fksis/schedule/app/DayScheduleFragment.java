package by.fksis.schedule.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DayScheduleFragment extends Fragment implements DatabaseObserver {
    private Calendar date;
    private int weekNumber, dayOfWeek;
    private DateFormat sdf_all = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private DateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

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
        currentClass.setText(getString(R.string.no_classes));
        nextClass.setText(getString(R.string.no_classes));

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.set(Calendar.HOUR, 0);
        time.set(Calendar.MINUTE, 00);
        time.set(Calendar.AM_PM, Calendar.AM);
        time.add(Calendar.DATE, 1);

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
        boolean found = false;
        for (ScheduleClass clazz : classes) {
            View lineView = LayoutInflater.from(getActivity()).inflate(R.layout.day_schedule_line, container, false);
            LineViewHolder holder = new LineViewHolder();
            Aibolit.doInjections(holder, lineView);

            holder.name.setText(clazz.name);
            holder.teacher.setText(Util.defaultValue(clazz.type, "") + " " + Util.defaultValue(clazz.teacher, ""));
            holder.time.setText(getResources().getStringArray(R.array.timeSlotStart)[clazz.timeSlot]);
            holder.time2.setText(getResources().getStringArray(R.array.timeSlotEnd)[clazz.timeSlot]);
            holder.room.setText(clazz.room);
            container.addView(lineView);
            Date dateStart = null ,dateEnd = null;
            try {
                dateStart = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime()) + " " + getResources().getStringArray(R.array.timeSlotStart)[clazz.timeSlot]);
                dateEnd = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime()) + " " + getResources().getStringArray(R.array.timeSlotEnd)[clazz.timeSlot]);
            } catch (Exception e){
                Log.e(DayScheduleFragment.class.getSimpleName(),e.getMessage());
            }
            if ((dateStart.getTime() <= Calendar.getInstance().getTime().getTime())
                    && (dateEnd.getTime() >= Calendar.getInstance().getTime().getTime())) {
                currentClass.setText(clazz.name + " " + ((clazz.room != null) ? clazz.room : ""));
                time.setTime(dateEnd);
                if ((classes.size() -1)  < classes.lastIndexOf(clazz))  {
                    ScheduleClass l_next = classes.get(classes.lastIndexOf(clazz) +1);
                    nextClass.setText( l_next.name + l_next.room);
                } else {
                    nextClass.setText(getString(R.string.no_classes));
                }
                found = true;
            }
        }
        if (!found) {
            found = false;
            for (Iterator<ScheduleClass> i = classes.iterator(); i.hasNext() && !found; ) {
                ScheduleClass l = i.next();
                Date dateStart = null, dateEnd;
                try {
                    dateStart = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime()) + " " + getResources().getStringArray(R.array.timeSlotStart)[l.timeSlot]);
                } catch (Exception e){
                    Log.e(DayScheduleFragment.class.getSimpleName(),e.getMessage());
                }
                if (dateStart.getTime() > Calendar.getInstance().getTime().getTime()) {
                    found = true;
                    currentClass.setText(getString(R.string.no_classes));
                    nextClass.setText(l.name + " " + ((l.room != null) ? l.room : ""));
                    time.setTime(dateStart);
                }
            }
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

    @InjectView(R.id.currentClass)
    private TextView currentClass;

    @InjectView(R.id.nextClass)
    private TextView nextClass;

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
