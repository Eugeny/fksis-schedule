package by.fksis.schedule;

import by.fksis.schedule.models.ScheduleClass;

import java.util.ArrayList;
import java.util.List;

public final class Database {
    public static List<String> getGroupList() {
        List<String> result = new ArrayList<String>();
        for (ScheduleClass clazz : ScheduleClass.get(ScheduleClass.class).groupBy("studentGroup").distinct().list())
            result.add(clazz.studentGroup);
        return result;
    }
}
