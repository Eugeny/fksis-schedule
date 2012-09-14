package by.fksis.schedule.app;

import android.content.Intent;
import android.os.Bundle;
import by.fksis.schedule.API;
import by.fksis.schedule.Preferences;
import by.fksis.schedule.R;
import com.WazaBe.HoloEverywhere.preference.ListPreference;
import com.WazaBe.HoloEverywhere.preference.Preference;
import com.WazaBe.HoloEverywhere.preference.SharedPreferences;
import com.WazaBe.HoloEverywhere.sherlock.SPreferenceActivity;

@SuppressWarnings("deprecation")
public class PreferenceActivity extends SPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        {
            Preference preference = findPreference(getString(R.string.log_out_preference));
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    API.updateCredentials(PreferenceActivity.this, null, null);
                    Intent intent = new Intent(PreferenceActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
            });
        }

        {
            ListPreference preference = (ListPreference) findPreference(getString(R.string.subgroup_preference));
<<<<<<< HEAD
            String[] subGroups = getStringArray(R.array.subgroups);
            int valueIndex = 0;
            preference.setSummary(subGroups[valueIndex + 1]);
            preference.setEntries(subGroups);
            preference.setEntryValues(subGroups);
            preference.setValueIndex(valueIndex + 1);
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(newValue.toString());
                    return false;
                }
            });
=======
            preference.setSummary(getResources().getStringArray(R.array.subgroups)[new Preferences(this).getSubgroup()]);
>>>>>>> upstream/master
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        ListPreference preference = (ListPreference) findPreference(getString(R.string.subgroup_preference));
        preference.setSummary(preference.getEntry());
    }

}
