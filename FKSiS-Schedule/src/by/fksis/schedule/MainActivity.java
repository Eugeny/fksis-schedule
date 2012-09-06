package by.fksis.schedule;

import android.content.Intent;
import android.os.Bundle;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;

public class MainActivity extends SActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
