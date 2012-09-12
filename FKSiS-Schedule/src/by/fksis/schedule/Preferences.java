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
}
