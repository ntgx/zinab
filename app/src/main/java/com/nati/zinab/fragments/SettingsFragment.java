package com.nati.zinab.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.nati.zinab.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        ListPreference langPref = (ListPreference) findPreference("langpref");
        if (langPref != null) {
            langPref.setNegativeButtonText(getString(R.string.cancel));
            langPref.setSummary(langPref.getEntry());
            langPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ListPreference listPreference = (ListPreference)preference;
                    CharSequence[] entries = listPreference.getEntries();
                    preference.setSummary(entries[listPreference.findIndexOfValue(newValue.toString())]);
                    return true;
                }
            });
        }

        ListPreference caltypePref = (ListPreference) findPreference("caltype");
        if (caltypePref != null) {
            caltypePref.setNegativeButtonText(getString(R.string.cancel));
            caltypePref.setSummary(caltypePref.getEntry());
            caltypePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ListPreference listPreference = (ListPreference)preference;
                    CharSequence[] entries = listPreference.getEntries();
                    preference.setSummary(entries[listPreference.findIndexOfValue(newValue.toString())]);
                    return true;
                }
            });
        }
    }
}