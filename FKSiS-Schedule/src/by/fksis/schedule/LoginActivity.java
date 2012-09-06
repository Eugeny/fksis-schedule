package by.fksis.schedule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.WazaBe.HoloEverywhere.AlertDialog;
import com.WazaBe.HoloEverywhere.FontLoader;
import com.WazaBe.HoloEverywhere.app.Activity;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.danikula.aibolit.Aibolit;

public class LoginActivity extends SActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = FontLoader.inflate(this, R.layout.fragment_login);
        Aibolit.doInjections(this, view);
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(R.string.log_in, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNeutralButton(R.string.scan_code, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}