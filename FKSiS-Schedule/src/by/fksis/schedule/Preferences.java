package by.fksis.schedule;

import android.content.Context;
import com.WazaBe.HoloEverywhere.preference.PreferenceManager;
import com.WazaBe.HoloEverywhere.preference.SharedPreferences;

public final class Preferences {
    private SharedPreferences preferences;
    private Context context;

    public Preferences(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getAutoReload() {
        return preferences.getBoolean(context.getString(R.string.reload_preference), true);
    }

    public int getSubgroup() {
        return Integer.parseInt(preferences.getString(context.getString(R.string.subgroup_preference), "0"));
    }

    public String getSubgroupString() {
        String value = preferences.getString(context.getString(R.string.subgroup_preference), "0");
        if (value.equals("0"))
            return "";
        return value;
    }

    public String getGroup() {
        return preferences.getString(context.getString(R.string.group_preference), "000000");
    }

    public void setGroup(String value) {
        preferences.edit().putString(context.getString(R.string.group_preference), value).commit();
    }

    public String getUserRole() {
        return preferences.getString(context.getString(R.string.role_preference), "none");
    }

    public void setUserRole(String value) {
        preferences.edit().putString(context.getString(R.string.role_preference), value).commit();
    }
}
