package by.fksis.schedule.app;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import by.fksis.schedule.API;
import by.fksis.schedule.R;
import com.WazaBe.HoloEverywhere.Toast;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.danikula.aibolit.Aibolit;
import com.danikula.aibolit.annotation.InjectView;
import com.danikula.aibolit.annotation.InjectOnClickListener;
import java.util.Date;

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
        Date date = new Date(dateText.getYear() - 1900, dateText.getMonth(), dateText.getDayOfMonth());
        if (editText.getText().toString().equals("") || date.before(new Date())) {
            Toast.makeText(this, getString(R.string.invalid_message), android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            API.queryAddBroadcasts(editText.getText().toString(), Long.toString(date.getTime()));
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
