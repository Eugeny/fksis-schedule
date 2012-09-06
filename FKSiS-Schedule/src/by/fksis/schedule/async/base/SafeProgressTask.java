package by.fksis.schedule.async.base;

import android.content.Context;
import by.fksis.schedule.L;

public abstract class SafeProgressTask extends ProgressTask<Void, Exception> {
    public SafeProgressTask(Context context) {
        super(context);
    }

    @Override
    protected Exception doInBackground(Void... arg0) {
        try {
            process();
            return null;
        } catch (Exception e) {
            L.d(this + " crashed: " + e);
            return e;
        }
    }

    public abstract void process() throws Exception;

    @Override
    protected void onPostExecute(Exception result) {
        super.onPostExecute(result);
        try {
            if (result == null) {
                onSuccess();
            } else {
                onError(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSuccess() {
    }

    public void onError(Exception error) {
    }
}
