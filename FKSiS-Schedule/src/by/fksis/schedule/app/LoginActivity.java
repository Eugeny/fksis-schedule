package by.fksis.schedule.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import by.fksis.schedule.API;
import by.fksis.schedule.R;
import com.WazaBe.HoloEverywhere.AlertDialog;
import com.WazaBe.HoloEverywhere.FontLoader;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
                        doLogin(username.getText().toString(), accessKey.getText().toString());
                    }
                })
                .setNeutralButton(R.string.scan_code, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new IntentIntegrator(LoginActivity.this).initiateScan();
                    }
                })
                .show();

        API.loadCredentials(this);
        if (API.credentialsPresent())
            username.setText(API.getUsername());
    }

    private void doLogin(String username, String accessKey) {
        API.updateCredentials(this, username, accessKey);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String contents = scanResult.getContents();
            String username = contents.split(":")[0];
            String key = contents.split(":")[1];
            doLogin(username, key);
        }
    }

    @InjectView(R.id.username)
    private EditText username;

    @InjectView(R.id.access_key)
    private EditText accessKey;
}