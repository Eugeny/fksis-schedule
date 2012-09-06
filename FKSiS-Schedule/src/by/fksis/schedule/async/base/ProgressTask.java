package by.fksis.schedule.async.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import by.fksis.schedule.R;
import by.fksis.schedule.ui.LoaderView;

public abstract class ProgressTask<I, Result> extends AsyncTaskEx<I, String, Result> {
    protected Context context;
    protected ProgressDialog dialog;
    protected LoaderView loader;
    private double progress = 0;

    public ProgressTask(Context ctx) {
        context = ctx;
    }

    public ProgressTask<I, Result> useLoader(View parent) {
        return useLoader((RelativeLayout) parent, true);
    }

    public ProgressTask<I, Result> useLoader(View parent, boolean bg) {
        if (parent != null)
            loader = LoaderView.createOn((RelativeLayout) parent, bg);
        return this;
    }

    public ProgressTask<I, Result> useProgressDialog() {
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.please_wait));
        dialog.show();
        return this;
    }

    public ProgressTask<I, Result> useHorizontalProgressDialog() {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        return this;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (dialog != null) {
            if (values == null) {
                dialog.setProgress((int) (progress * 100));
            } else {
                dialog.setMessage(values[0]);
            }
        }
    }

    protected void publishProgress(int resid) {
        publishProgress(context.getString(resid));
    }


    protected void onPostExecute(Result result) {
        if (dialog != null)
            dialog.dismiss();
        if (loader != null)
            loader.finish();
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
