package by.fksis.schedule.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import by.fksis.schedule.R;
import com.WazaBe.HoloEverywhere.Toast;
import com.WazaBe.HoloEverywhere.View;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectView;
import com.danikula.aibolit.annotation.InjectOnClickListener;

public class MessageActivity extends SActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Aibolit.doInjections(this);
    }

    @InjectView(R.id.message_text)
    private EditText editText;

    @InjectView(R.id.message_date)
    private DatePicker dateText;

    @InjectOnClickListener(R.id.message_btn)
    public void onClick(android.view.View v) {
        Toast.makeText(this, "Test", android.widget.Toast.LENGTH_LONG).show();
    }
}
