package by.fksis.schedule;

import android.util.Log;

public class L {
    public static String tag = "L";

    public static String getLocation(int d) {
        StackTraceElement e = Thread.currentThread().getStackTrace()[d];
        return e.getClassName() + "." + e.getMethodName() + "():" + e.getLineNumber();
    }

    private static String prepareLine(Object l) {
        return getLocation(5) + " " + l;
    }

    public static void d(Object l) {
        Log.d(tag, prepareLine(l));
    }
}
