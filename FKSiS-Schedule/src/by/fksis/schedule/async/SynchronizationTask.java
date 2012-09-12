package by.fksis.schedule.async;

import android.content.Context;
import by.fksis.schedule.API;
import by.fksis.schedule.L;
import by.fksis.schedule.R;
import by.fksis.schedule.async.base.SafeProgressTask;
import by.fksis.schedule.models.ScheduleClass;
import com.WazaBe.HoloEverywhere.Toast;
import com.ormy.Application;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        // TODO: Save user data!
        L.d(userData.toString());

        JSONArray jsonClasses = API.queryClasses();
        ScheduleClass[] classes = new ScheduleClass[jsonClasses.length()];
        int length = jsonClasses.length();
        for (int i = 0; i < length; i++)
            classes[i] = new ScheduleClass(context, jsonClasses.getJSONObject(i));

        // Delete old data
        ScheduleClass.get(ScheduleClass.class).delete();
        Application.getDatabase().sql.beginTransaction();
        for (int i = 0; i < length; i++) {
            classes[i].save();
            if (i % 30 == 0) {
                setProgress(1.0 * i / length);
                publishProgress((String[]) null);
            }
        }
        Application.getDatabase().sql.setTransactionSuccessful();
        Application.getDatabase().sql.endTransaction();
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
