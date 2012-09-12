package by.fksis.schedule;

import android.content.Context;
import com.WazaBe.HoloEverywhere.preference.PreferenceManager;
import com.WazaBe.HoloEverywhere.preference.SharedPreferences;

public final class Preferences {
    private static final String PREFERENCE_AUTO_RELOAD = "auto_reload";
    private static final String PREFERENCE_SUBGROUP = "subgroup";

    private SharedPreferences preferences;

    public Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getAutoReload() {
        return preferences.getBoolean(PREFERENCE_AUTO_RELOAD, true);
    }

    public String getSubgroup() {
        return preferences.getString(PREFERENCE_SUBGROUP, "1,2");
    }
}
