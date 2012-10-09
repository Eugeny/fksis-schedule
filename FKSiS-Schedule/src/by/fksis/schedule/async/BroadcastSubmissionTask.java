package by.fksis.schedule.async;

import android.content.Context;
import by.fksis.schedule.API;
import by.fksis.schedule.R;
import by.fksis.schedule.async.base.SafeProgressTask;
import com.WazaBe.HoloEverywhere.Toast;

public class BroadcastSubmissionTask extends SafeProgressTask {
    private String text;
    private long expires;

    public BroadcastSubmissionTask(Context context, String text, long expires) {
        super(context);
        this.text = text;
        this.expires = expires;
        useProgressDialog();
    }

    @Override
    public void process() throws Exception {
        API.queryAddBroadcast(text, expires);
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
