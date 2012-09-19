package by.fksis.schedule.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import by.fksis.schedule.API;
import by.fksis.schedule.Preferences;
import by.fksis.schedule.R;
import by.fksis.schedule.Util;
import by.fksis.schedule.dal.ScheduleClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Widget extends AppWidgetProvider {
    public static final String TAG = "Widget";
    public static String noLesson = "Нет занятия";
    public static int WIDGET_ID = 1;

    @Override
    public void onEnabled(Context context) {
        Intent updaterIntent = new Intent();
        updaterIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updaterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { WIDGET_ID });
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updaterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1000, pendingIntent);
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] ids) {
        for (int id : ids) {
            Intent intent = new Intent();
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { id });
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
        super.onDeleted(context, ids);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context, Widget.class);
        DateFormat sdf_all = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        DateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Calendar time = Calendar.getInstance();
        int weekNumber, dayOfWeek;
        dayOfWeek = Util.getDayOfWeekIndex(time);
        weekNumber = Util.getScheduleWeek(time.getTime());
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            remoteViews.setTextViewText(R.id.widget_curent, noLesson);
            remoteViews.setTextViewText(R.id.widget_next, noLesson);
            time.setTimeInMillis(System.currentTimeMillis());
            time.set(Calendar.HOUR, 0);
            time.set(Calendar.MINUTE, 00);
            time.set(Calendar.AM_PM, Calendar.AM);
            time.add(Calendar.DATE, 1);
            try {
                API.loadCredentials(context);
                if (API.credentialsPresent()) {
                    List<ScheduleClass> classes = ScheduleClass.get(ScheduleClass.class)
                            .filter("weeks%", "%" + weekNumber + "%")
                            .filter("day", dayOfWeek)
                            .filter("studentGroup", new Preferences(context).getGroup())
                            .filter("subgroups%", "%" + new Preferences(context).getSubgroupString() + "%")
                            .list();

                    if ((classes != null) && (classes.size() > 0)) {
                        boolean find = false;
                        for (Iterator<ScheduleClass> i = classes.iterator(); i.hasNext() && !find;) {
                            ScheduleClass l = i.next();
                            Date dateStart = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime())+ " " + context.getResources().getStringArray(R.array.timeSlotStart)[l.timeSlot]);
                            Date dateEnd = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime())+ " " + context.getResources().getStringArray(R.array.timeSlotEnd)[l.timeSlot]);
                            if ((dateStart.getTime() <= Calendar.getInstance().getTime().getTime())
                                    && (dateEnd.getTime() >= Calendar.getInstance().getTime().getTime())) {
                                remoteViews.setTextViewText(R.id.widget_curent, l.name + " " + ((l.room != null)? l.room : "") );
                                time.setTime(dateEnd);
                                if (i.hasNext()) {
                                    ScheduleClass l_next = i.next();
                                    remoteViews.setTextViewText(R.id.widget_next, l_next.name + l_next.room);
                                } else {
                                    remoteViews.setTextViewText(R.id.widget_next, noLesson);
                                }
                                find = true;
                            }

                        }
                        if (!find) {
                            find = false;
                            for (Iterator<ScheduleClass> i = classes.iterator(); i.hasNext() && !find;) {
                                ScheduleClass l = i.next();
                                Date dateStart = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime())+ " " + context.getResources().getStringArray(R.array.timeSlotStart)[l.timeSlot]);
                                Date dateEnd = sdf_all.parse(sdf_date.format(Calendar.getInstance().getTime())+ " " + context.getResources().getStringArray(R.array.timeSlotEnd)[l.timeSlot]);
                                if (dateStart.getTime() > Calendar.getInstance().getTime().getTime()) {
                                    find = true;
                                    remoteViews.setTextViewText(R.id.widget_curent, noLesson);
                                    remoteViews.setTextViewText(R.id.widget_next, l.name + " " + ((l.room != null)? l.room : ""));
                                    time.setTime(dateStart);
                                }
                            }
                        }
                    }
                }
                //Log.d("BSUIR", Lesson.formatter.format(time.getTime()));
            } catch (Exception e) {
                Log.e("BSUIR", e.getMessage());
            } finally {
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
        for (int id : appWidgetIds) {
            Intent intent = new Intent();
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { id});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.RTC, time.getTimeInMillis(), pendingIntent);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
