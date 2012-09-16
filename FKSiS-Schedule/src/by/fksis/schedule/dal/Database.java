package by.fksis.schedule.dal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Database {
    public static boolean autoRefresh = true;

    public static List<String> getGroupList() {
        List<String> result = new ArrayList<String>();
        for (ScheduleClass clazz : ScheduleClass.get(ScheduleClass.class).groupBy("studentGroup").distinct().list())
            result.add(clazz.studentGroup);
        Collections.sort(result);
        return result;
    }
}
