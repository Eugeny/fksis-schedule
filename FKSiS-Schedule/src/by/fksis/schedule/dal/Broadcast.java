package by.fksis.schedule.dal;

import android.content.Context;
import com.ormy.Model;
import com.ormy.annotations.Column;
import com.ormy.annotations.Table;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

@Table
public class Broadcast extends Model<Broadcast> {
    @Column
    public Long remoteId = -1L;

    @Column
    public String text;

    @Column
    public String groupList;

    @Column
    public String expires;


    public Broadcast(Context ctx) {
        super(ctx);
    }

    public Broadcast(Context ctx, JSONObject json) {
        super(ctx);
        try {
            remoteId = json.getLong("id");
            text = json.getString("text");
            groupList = json.getString("groupList");
            expires = json.getString("expires");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isExpired() {
        return new Date(Date.parse(expires)).before(new Date());
    }
}
