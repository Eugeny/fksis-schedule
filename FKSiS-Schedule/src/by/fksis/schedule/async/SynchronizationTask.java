package by.fksis.schedule.async;

import android.content.Context;
import by.fksis.schedule.API;
import by.fksis.schedule.L;
import by.fksis.schedule.Preferences;
import by.fksis.schedule.R;
import by.fksis.schedule.async.base.SafeProgressTask;
import by.fksis.schedule.dal.Broadcast;
import by.fksis.schedule.dal.Database;
import by.fksis.schedule.dal.ScheduleClass;
import com.WazaBe.HoloEverywhere.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

public class SynchronizationTask extends SafeProgressTask {
    public SynchronizationTask(Context context, boolean foreground) {
        super(context);

        // Go foreground for the first sync
        if (ScheduleClass.get(ScheduleClass.class).count() == 0)
            foreground = true;

        if (foreground)
            useHorizontalProgressDialog();
    }

    @Override
    public void process() throws Exception {
        JSONObject userData = API.queryUserData();
        Preferences preferences = new Preferences(context);
        if (preferences.getGroup().equals("000000"))
            preferences.setGroup(userData.getJSONObject("student").getString("groupNumber"));

        JSONArray jsonBroadcasts = API.queryBroadcasts();
        int bLength = jsonBroadcasts.length();
        Database.beginTransaction();
        Broadcast.get(Broadcast.class).delete();
        for (int i = 0; i < bLength; i++) {
            new Broadcast(context, jsonBroadcasts.getJSONObject(i)).save();
            setProgress(1.0 * i / bLength);
            publishProgress((String[]) null);
        }
        Database.endTransaction();

        JSONArray jsonClasses = API.queryClasses();
        int cLength = jsonClasses.length();

        // Delete old data
        Database.beginTransaction();
        ScheduleClass.get(ScheduleClass.class).delete();
        for (int i = 0; i < cLength; i++) {
            new ScheduleClass(context, jsonClasses.getJSONObject(i)).save();
            if (i % 30 == 0) {
                setProgress(1.0 * i / cLength);
                publishProgress((String[]) null);
            }
        }
        Database.endTransaction();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(context, R.string.synchronization_complete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception error) {
        Toast.makeText(context, R.string.synchronization_failed, Toast.LENGTH_SHORT).show();
    }
}
