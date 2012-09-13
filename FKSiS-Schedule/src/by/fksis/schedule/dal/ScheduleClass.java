package by.fksis.schedule.dal;

import android.content.Context;
import com.ormy.Model;
import com.ormy.annotations.Column;
import com.ormy.annotations.SortBy;
import com.ormy.annotations.Table;
import org.json.JSONException;
import org.json.JSONObject;

@Table
public class ScheduleClass extends Model<ScheduleClass> {
    @Column
    public Long remoteId = -1L;

    @Column
    public String name;

    @Column
    public String weeks;

    @Column
    public String subgroups;

    @Column
    public int day;

    @Column
    @SortBy
    public int timeSlot;

    @Column
    public String studentGroup;

    @Column
    public String room;

    @Column
    public String type;

    @Column
    public String teacher;

    public ScheduleClass(Context ctx) {
        super(ctx);
    }

    public ScheduleClass(Context ctx, JSONObject json) {
        super(ctx);
        try {
            remoteId = json.getLong("id");
            name = json.getString("name");
            weeks = json.getString("weeks");
            subgroups = json.getString("subgroups");
            day = json.getInt("day");
            timeSlot = json.getInt("timeSlot");
            studentGroup = json.getString("group");
            if (json.has("room"))
                room = json.getString("room");
            if (json.has("type"))
                type = json.getString("type");
            if (json.has("teacher"))
                teacher = json.getString("teacher");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
